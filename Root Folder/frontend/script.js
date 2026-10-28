/* ===================================================================
   Rescue Dispatch — frontend v1.2
   Same domain model as the Java backend (Citizen, Emergency,
   RescueTeam, DisasterManagementAuthority).

   BACKEND HANDOFF: set CONFIG.USE_BACKEND = true and every action is
   also sent to your friend's API (see sync() calls below):
     POST /emergencies               {citizenName,type,location,severity,peopleAffected}
     PUT  /emergencies/{id}/verify
     PUT  /emergencies/{id}/reject
     PUT  /emergencies/{id}/assign   {teamId}
     PUT  /teams/{id}/status         {status}
   =================================================================== */

const CONFIG = { USE_BACKEND: false, API_BASE: "http://localhost:8080/api" };
const STORAGE_KEY = "rescueDispatch.v1";
const CLOSED = ["Completed", "Rejected"];

const $ = id => document.getElementById(id);
const esc = s => String(s).replace(/[&<>"']/g, c => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
const clock = ms => new Date(ms).toLocaleTimeString();
const ago = ms => {
  const m = Math.floor((Date.now() - ms) / 60000);
  return m < 1 ? "just now" : m < 60 ? `${m}m ago` : `${Math.floor(m / 60)}h ${m % 60}m ago`;
};

/* ---------- Domain model ---------- */
class Emergency {
  constructor(id, type, location, severity, peopleAffected, reportedBy) {
    Object.assign(this, { id, type, location, severity, peopleAffected, reportedBy,
      status: "Reported", assignedTeam: null, createdAt: Date.now(),
      history: [{ status: "Reported", at: Date.now() }] });
  }
}

class RescueTeam {
  constructor(id, name) { this.id = id; this.name = name; this.status = "Available"; }
}

class DisasterManagementAuthority {
  constructor() { this.reset(); }

  reset() {
    this.emergencies = [];
    this.teams = [
      new RescueTeam("RT01", "Alpha Rescue Team"),
      new RescueTeam("RT02", "Bravo Rescue Team"),
      new RescueTeam("RT03", "Charlie Rescue Team"),
    ];
    this.log = [];
    this._nextId = 1;
  }

  serialize() { return { emergencies: this.emergencies, teams: this.teams, log: this.log, nextId: this._nextId }; }
  hydrate(d) { this.emergencies = d.emergencies; this.teams = d.teams; this.log = d.log; this._nextId = d.nextId; }

  find(id) { return this.emergencies.find(e => e.id === id); }
  team(id) { return this.teams.find(t => t.id === id); }

  _setStatus(e, status) { e.status = status; e.history.push({ status, at: Date.now() }); }
  _addLog(message) { this.log.unshift({ at: Date.now(), message }); this.log = this.log.slice(0, 100); }

  reportEmergency(citizenName, type, location, severity, peopleAffected) {
    const id = "E" + String(this._nextId++).padStart(3, "0");
    const e = new Emergency(id, type, location, severity, peopleAffected, citizenName);
    this.emergencies.unshift(e);
    this._addLog(`${citizenName} reported ${type} at ${location} (${severity})`);
    return e;
  }

  verifyEmergency(id) {
    const e = this.find(id);
    if (e && e.status === "Reported") { this._setStatus(e, "Verified"); this._addLog(`${id} verified by authority`); }
  }

  rejectEmergency(id) {
    const e = this.find(id);
    if (e && ["Reported", "Verified"].includes(e.status)) { this._setStatus(e, "Rejected"); this._addLog(`${id} rejected as invalid`); }
  }

  assignTeam(emergencyId, teamId) {
    const e = this.find(emergencyId), team = this.team(teamId);
    if (!e || !team || e.status !== "Verified" || team.status !== "Available") return false;
    team.status = "En Route";
    e.assignedTeam = team.id;
    this._setStatus(e, "Assigned");
    this._addLog(`${team.name} assigned to ${emergencyId}`);
    return true;
  }

  updateTeamStatus(teamId, status) {
    const team = this.team(teamId);
    if (!team) return;
    team.status = status;
    // Only the team's current open incident — fixes matching an old completed one
    const e = this.emergencies.find(x => x.assignedTeam === teamId && !CLOSED.includes(x.status));
    if (e) this._setStatus(e, status);
    if (status === "Completed") team.status = "Available";
    this._addLog(`${team.name} status -> ${status}`);
  }

  freeTeamCount() { return this.teams.filter(t => t.status === "Available").length; }
}

const authority = new DisasterManagementAuthority();
const filters = { q: "", status: "active", severity: "all" };
let trackedId = "";

/* ---------- Persistence, backend sync, toasts ---------- */
function save() {
  try { localStorage.setItem(STORAGE_KEY, JSON.stringify(authority.serialize())); } catch (_) { /* storage full or blocked */ }
}

function load() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (raw) authority.hydrate(JSON.parse(raw));
  } catch (_) { authority.reset(); }
}

function sync(method, path, body) {
  if (!CONFIG.USE_BACKEND) return;
  fetch(CONFIG.API_BASE + path, {
    method, headers: { "Content-Type": "application/json" }, body: body ? JSON.stringify(body) : undefined,
  }).then(r => { if (!r.ok) throw new Error(r.status); })
    .catch(() => toast("Backend unreachable — change saved locally only", "err"));
}

function toast(message, type = "ok") {
  const t = document.createElement("div");
  t.className = "toast " + type;
  t.textContent = message;
  $("toasts").append(t);
  setTimeout(() => t.remove(), 4000);
}

function commit() { save(); renderAll(); }

/* ---------- Tabs (click, arrow keys, URL hash) ---------- */
const tabs = [...document.querySelectorAll(".tab")];

function showTab(name, focus = false) {
  tabs.forEach(t => {
    const on = t.dataset.tab === name;
    t.classList.toggle("active", on);
    t.setAttribute("aria-selected", on);
    t.tabIndex = on ? 0 : -1;
    if (on && focus) t.focus();
  });
  document.querySelectorAll(".panel").forEach(p => p.classList.toggle("hidden", p.dataset.panel !== name));
  history.replaceState(null, "", "#" + name);
}

tabs.forEach((tab, i) => {
  tab.addEventListener("click", () => showTab(tab.dataset.tab));
  tab.addEventListener("keydown", ev => {
    const step = { ArrowRight: 1, ArrowLeft: -1 }[ev.key];
    if (step) showTab(tabs[(i + step + tabs.length) % tabs.length].dataset.tab, true);
  });
});

/* ---------- Citizen: report form ---------- */
function formError(msg, field) {
  const s = $("citizenStatus");
  s.textContent = msg; s.classList.add("error");
  if (field) { field.setAttribute("aria-invalid", "true"); field.focus(); }
}

$("reportForm").addEventListener("submit", ev => {
  ev.preventDefault();
  const nameEl = $("citizenName"), locEl = $("location"), affEl = $("affected");
  [nameEl, locEl, affEl].forEach(f => f.removeAttribute("aria-invalid"));
  $("citizenStatus").classList.remove("error");

  const name = nameEl.value.trim(), location = locEl.value.trim(), affected = parseInt(affEl.value, 10);
  if (name.length < 2) return formError("Enter your name (at least 2 characters).", nameEl);
  if (!location) return formError("Enter where the emergency is happening.", locEl);
  if (!(affected >= 1 && affected <= 10000)) return formError("People affected must be between 1 and 10,000.", affEl);

  const type = $("type").value, severity = $("severity").value;
  const e = authority.reportEmergency(name, type, location, severity, affected);
  sync("POST", "/emergencies", { citizenName: name, type, location, severity, peopleAffected: affected });

  $("citizenStatus").textContent = `Reported as ${e.id} — keep this ID to track your report.`;
  toast(`Report ${e.id} sent to the authority`);
  ev.target.reset();
  $("severity").value = "Medium";
  $("affected").value = 1;
  trackedId = e.id;
  $("trackId").value = e.id;
  commit();
});

$("geoBtn").addEventListener("click", () => {
  if (!navigator.geolocation) return toast("Location is not supported in this browser", "err");
  navigator.geolocation.getCurrentPosition(
    p => { $("location").value = `${p.coords.latitude.toFixed(4)}, ${p.coords.longitude.toFixed(4)}`; },
    () => toast("Could not get your location — type it in instead", "err"),
    { timeout: 8000 });
});

/* ---------- Citizen: track a report ---------- */
$("trackForm").addEventListener("submit", ev => {
  ev.preventDefault();
  trackedId = $("trackId").value.trim().toUpperCase();
  renderTracker();
});

function renderTracker() {
  const el = $("trackResult");
  if (!trackedId) { el.innerHTML = ""; return; }
  const e = authority.find(trackedId);
  if (!e) { el.innerHTML = `<p class="empty-note">No report found with ID ${esc(trackedId)}. Check the ID and try again.</p>`; return; }
  const team = e.assignedTeam ? authority.team(e.assignedTeam) : null;
  el.innerHTML = `
    <p class="card-meta">${esc(e.id)} — ${esc(e.type)} at ${esc(e.location)}${team ? ` · ${esc(team.name)}` : ""}</p>
    <ol class="timeline">${e.history.map(h => `<li>${esc(h.status)}<time>${clock(h.at)}</time></li>`).join("")}</ol>`;
}

/* ---------- Authority: filters, actions ---------- */
$("filterQ").addEventListener("input", ev => { filters.q = ev.target.value.trim().toLowerCase(); renderAuthority(); });
$("filterStatus").addEventListener("change", ev => { filters.status = ev.target.value; renderAuthority(); });
$("filterSeverity").addEventListener("change", ev => { filters.severity = ev.target.value; renderAuthority(); });

const priority = e => ({ High: 3, Medium: 2, Low: 1 }[e.severity]) * 1000 + Math.min(e.peopleAffected, 999);

function visibleEmergencies() {
  const { q, status, severity } = filters;
  return authority.emergencies
    .filter(e => !q || `${e.id} ${e.type} ${e.location} ${e.reportedBy}`.toLowerCase().includes(q))
    .filter(e => status === "all" || (status === "active" ? !CLOSED.includes(e.status) : e.status === status))
    .filter(e => severity === "all" || e.severity === severity)
    .sort((a, b) => CLOSED.includes(a.status) - CLOSED.includes(b.status) || priority(b) - priority(a) || a.createdAt - b.createdAt);
}

document.addEventListener("click", ev => {
  const btn = ev.target.closest("[data-verify],[data-reject],[data-assign],[data-status]");
  if (!btn) return;
  const d = btn.dataset;
  if (d.verify) { authority.verifyEmergency(d.verify); sync("PUT", `/emergencies/${d.verify}/verify`); toast(`${d.verify} verified`); }
  if (d.reject) {
    if (!confirm(`Reject ${d.reject} as an invalid report?`)) return;
    authority.rejectEmergency(d.reject); sync("PUT", `/emergencies/${d.reject}/reject`); toast(`${d.reject} rejected`);
  }
  if (d.assign) {
    const [eid, tid] = d.assign.split("|");
    if (authority.assignTeam(eid, tid)) { sync("PUT", `/emergencies/${eid}/assign`, { teamId: tid }); toast(`${authority.team(tid).name} assigned to ${eid}`); }
    else toast("That team is no longer available", "err");
  }
  if (d.status) {
    const [tid, status] = d.status.split("|");
    authority.updateTeamStatus(tid, status); sync("PUT", `/teams/${tid}/status`, { status });
    toast(`${authority.team(tid).name}: ${status}`);
  }
  commit();
});

$("exportBtn").addEventListener("click", () => {
  const rows = [["ID", "Type", "Location", "Severity", "People", "Reported by", "Status", "Team", "Reported at"]]
    .concat(authority.emergencies.map(e => [e.id, e.type, e.location, e.severity, e.peopleAffected, e.reportedBy, e.status, e.assignedTeam || "", new Date(e.createdAt).toISOString()]));
  const csv = rows.map(r => r.map(c => `"${String(c).replace(/"/g, '""')}"`).join(",")).join("\n");
  const a = Object.assign(document.createElement("a"), { href: URL.createObjectURL(new Blob([csv], { type: "text/csv" })), download: "emergencies.csv" });
  a.click(); URL.revokeObjectURL(a.href);
});

function seedDemo() {
  const demo = [["Rahul Sharma", "Flood", "Vadodara, Alkapuri", "High", 40], ["Meera Patel", "Fire", "Surat, Adajan", "High", 12],
    ["Imran Khan", "Road Accident", "NH48, Bharuch", "Medium", 3], ["Anita Joshi", "Building Collapse", "Ahmedabad, Maninagar", "High", 25]];
  demo.forEach(d => authority.reportEmergency(...d));
  authority.verifyEmergency(authority.emergencies[demo.length - 1].id);
  toast("Demo data loaded");
  commit();
}

$("demoBtn").addEventListener("click", seedDemo);
$("resetBtn").addEventListener("click", () => {
  if (!confirm("Delete all incidents, teams' progress and the log?")) return;
  authority.reset(); trackedId = ""; $("trackId").value = ""; toast("All data cleared"); commit();
});

/* ---------- Rendering ---------- */
function renderAll() { renderStats(); renderAuthority(); renderTeams(); renderLog(); renderTracker(); }

function renderStats() {
  const open = authority.emergencies.filter(e => !CLOSED.includes(e.status));
  $("statActive").textContent = open.length;
  $("statCritical").textContent = open.filter(e => e.severity === "High").length;
  $("statTeams").textContent = authority.freeTeamCount();
  $("statDone").textContent = authority.emergencies.filter(e => e.status === "Completed").length;
  const waiting = authority.emergencies.filter(e => e.status === "Reported").length;
  $("queueCount").textContent = waiting;
  $("queueCount").hidden = waiting === 0;
}

function renderAuthority() {
  const el = $("authorityQueue");
  const list = visibleEmergencies();
  if (list.length === 0) {
    el.innerHTML = authority.emergencies.length === 0
      ? `<p class="empty-note">No emergencies reported yet. Submit one from the Report tab, or use “Load demo data” below.</p>`
      : `<p class="empty-note">No incidents match these filters.</p>`;
    return;
  }
  el.innerHTML = list.map(e => `
    <div class="card sev-${esc(e.severity)} ${CLOSED.includes(e.status) ? "closed" : ""}">
      <div class="card-main">
        <span class="card-title">${esc(e.id)} — ${esc(e.type)} at ${esc(e.location)}</span>
        <span class="card-meta">${e.peopleAffected} affected · reported by ${esc(e.reportedBy)} · <span data-age="${e.createdAt}">${ago(e.createdAt)}</span></span>
      </div>
      <span class="badge ${esc(e.severity)}">${esc(e.severity)}</span>
      <span class="badge ${esc(e.status.replace(/\s/g, ""))}">${esc(e.status)}</span>
      <div class="card-actions">
        ${e.status === "Reported" ? `<button class="btn-small" data-verify="${e.id}">Verify</button>` : ""}
        ${["Reported", "Verified"].includes(e.status) ? `<button class="btn-small btn-danger" data-reject="${e.id}">Reject</button>` : ""}
        ${e.status === "Verified" ? teamAssignButtons(e.id) : ""}
      </div>
    </div>`).join("");
}

function teamAssignButtons(emergencyId) {
  const free = authority.teams.filter(t => t.status === "Available");
  if (free.length === 0) return `<span class="empty-note">No teams free — wait for one to finish</span>`;
  return free.map(t => `<button class="btn-small" data-assign="${emergencyId}|${t.id}">Assign ${esc(t.name)}</button>`).join("");
}

function renderTeams() {
  $("teamList").innerHTML = authority.teams.map(t => {
    const next = { "En Route": "In Action", "In Action": "Completed" }[t.status];
    const job = authority.emergencies.find(x => x.assignedTeam === t.id && !CLOSED.includes(x.status));
    return `
      <div class="card">
        <div class="card-main">
          <span class="card-title">${esc(t.name)}</span>
          <span class="card-meta">${esc(t.id)}${job ? ` · ${esc(job.id)}: ${esc(job.type)} at ${esc(job.location)}` : " · standing by"}</span>
        </div>
        <span class="badge ${esc(t.status.replace(/\s/g, ""))}">${esc(t.status)}</span>
        <div class="card-actions">${next ? `<button class="btn-small" data-status="${t.id}|${next}">Mark ${next}</button>` : ""}</div>
      </div>`;
  }).join("");
}

function renderLog() {
  $("log").innerHTML = authority.log.length === 0
    ? `<p class="empty-note">Activity will appear here.</p>`
    : authority.log.map(l => `<div class="log-entry"><div class="log-time">${clock(l.at)}</div><div>${esc(l.message)}</div></div>`).join("");
}

/* ---------- Live updates ---------- */
// Refresh "5m ago" labels without re-rendering (keeps button focus)
setInterval(() => document.querySelectorAll("[data-age]").forEach(s => { s.textContent = ago(+s.dataset.age); }), 30000);

// Another browser tab changed the data (e.g. citizen tab + authority tab side by side)
window.addEventListener("storage", ev => { if (ev.key === STORAGE_KEY) { load(); renderAll(); } });

/* ---------- Start ---------- */
load();
const startTab = location.hash.slice(1);
if (["citizen", "authority", "team"].includes(startTab)) showTab(startTab);
renderAll();