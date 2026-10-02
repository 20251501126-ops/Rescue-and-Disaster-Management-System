/* ===================================================================
   Same domain model as the Java backend: Citizen, Emergency,
   RescueTeam, DisasterManagementAuthority. This runs entirely in the
   browser (no server) so your group can demo the UI on its own; wire
   these classes up to real API calls later if you connect a backend.
   =================================================================== */

class Emergency {
  constructor(id, type, location, severity, peopleAffected, reportedBy) {
    this.id = id;
    this.type = type;
    this.location = location;
    this.severity = severity;
    this.peopleAffected = peopleAffected;
    this.reportedBy = reportedBy;
    this.status = "Reported";
    this.assignedTeam = null;
  }
}

class RescueTeam {
  constructor(id, name) {
    this.id = id;
    this.name = name;
    this.status = "Available";
  }
}

class DisasterManagementAuthority {
  constructor() {
    this.emergencies = [];
    this.teams = [
      new RescueTeam("RT01", "Alpha Rescue Team"),
      new RescueTeam("RT02", "Bravo Rescue Team"),
      new RescueTeam("RT03", "Charlie Rescue Team"),
    ];
    this.log = [];
    this._nextId = 1;
  }

  reportEmergency(citizenName, type, location, severity, peopleAffected) {
    const id = "E" + String(this._nextId++).padStart(3, "0");
    const emergency = new Emergency(id, type, location, severity, peopleAffected, citizenName);
    this.emergencies.unshift(emergency);
    this._addLog(`${citizenName} reported ${type} at ${location} (${severity})`);
    return emergency;
  }

  verifyEmergency(id) {
    const e = this.emergencies.find(e => e.id === id);
    if (e && e.status === "Reported") {
      e.status = "Verified";
      this._addLog(`${id} verified by authority`);
    }
  }

  assignTeam(emergencyId, teamId) {
    const e = this.emergencies.find(e => e.id === emergencyId);
    const team = this.teams.find(t => t.id === teamId);
    if (!e || !team || team.status !== "Available") return;
    team.status = "En Route";
    e.status = "Assigned";
    e.assignedTeam = team.id;
    this._addLog(`${team.name} assigned to ${emergencyId}`);
  }

  updateTeamStatus(teamId, status) {
    const team = this.teams.find(t => t.id === teamId);
    if (!team) return;
    team.status = status;
    const e = this.emergencies.find(e => e.assignedTeam === teamId);
    if (e) {
      e.status = status === "Completed" ? "Completed" : status;
      if (status === "Completed") team.status = "Available";
    }
    this._addLog(`${team.name} status -> ${status}`);
  }

  freeTeamCount() {
    return this.teams.filter(t => t.status === "Available").length;
  }

  _addLog(message) {
    this.log.unshift({ time: new Date().toLocaleTimeString(), message });
  }
}

const authority = new DisasterManagementAuthority();

/* ---------- Tabs ---------- */
document.querySelectorAll(".tab").forEach(tab => {
  tab.addEventListener("click", () => {
    document.querySelectorAll(".tab").forEach(t => { t.classList.remove("active"); t.setAttribute("aria-selected", "false"); });
    tab.classList.add("active");
    tab.setAttribute("aria-selected", "true");
    document.querySelectorAll(".panel").forEach(p => p.classList.add("hidden"));
    document.getElementById(`panel-${tab.dataset.tab}`).classList.remove("hidden");
  });
});

/* ---------- Citizen: report form ---------- */
document.getElementById("reportForm").addEventListener("submit", (ev) => {
  ev.preventDefault();
  const name = document.getElementById("citizenName").value.trim();
  const type = document.getElementById("type").value;
  const location = document.getElementById("location").value.trim();
  const severity = document.getElementById("severity").value;
  const affected = parseInt(document.getElementById("affected").value, 10) || 1;

  if (!name || !location) {
    document.getElementById("citizenStatus").textContent = "Please fill in your name and location.";
    return;
  }

  const e = authority.reportEmergency(name, type, location, severity, affected);
  document.getElementById("citizenStatus").textContent = `Reported as ${e.id} — the authority has been notified.`;
  ev.target.reset();
  document.getElementById("severity").value = "Medium";
  document.getElementById("affected").value = 1;
  renderAll();
});

/* ---------- Rendering ---------- */
function renderAll() {
  renderStats();
  renderAuthority();
  renderTeams();
  renderLog();
}

function renderStats() {
  const active = authority.emergencies.filter(e => e.status !== "Completed").length;
  document.getElementById("statActive").textContent = active;
  document.getElementById("statTeams").textContent = authority.freeTeamCount();
}

function renderAuthority() {
  const el = document.getElementById("authorityQueue");
  if (authority.emergencies.length === 0) {
    el.innerHTML = `<p class="empty-note">No emergencies reported yet.</p>`;
    return;
  }
  el.innerHTML = authority.emergencies.map(e => `
    <div class="card">
      <div class="card-main">
        <span class="card-title">${e.id} — ${e.type} at ${e.location}</span>
        <span class="card-meta">${e.peopleAffected} affected · reported by ${e.reportedBy}</span>
      </div>
      <span class="badge ${e.severity}">${e.severity}</span>
      <span class="badge ${e.status.replace(/\s/g, "")}">${e.status}</span>
      <div class="card-actions">
        ${e.status === "Reported" ? `<button class="btn-small" data-verify="${e.id}">Verify</button>` : ""}
        ${e.status === "Verified" ? teamAssignButtons(e.id) : ""}
      </div>
    </div>
  `).join("");

  el.querySelectorAll("[data-verify]").forEach(btn =>
    btn.addEventListener("click", () => { authority.verifyEmergency(btn.dataset.verify); renderAll(); }));
  el.querySelectorAll("[data-assign]").forEach(btn =>
    btn.addEventListener("click", () => {
      const [eid, tid] = btn.dataset.assign.split("|");
      authority.assignTeam(eid, tid);
      renderAll();
    }));
}

function teamAssignButtons(emergencyId) {
  const free = authority.teams.filter(t => t.status === "Available");
  if (free.length === 0) return `<span class="empty-note">No teams free — queued</span>`;
  return free.map(t => `<button class="btn-small" data-assign="${emergencyId}|${t.id}">Assign ${t.name}</button>`).join("");
}

function renderTeams() {
  const el = document.getElementById("teamList");
  el.innerHTML = authority.teams.map(t => {
    const next = { "Available": null, "En Route": "In Action", "In Action": "Completed", "Completed": null }[t.status];
    return `
      <div class="card">
        <div class="card-main">
          <span class="card-title">${t.name}</span>
          <span class="card-meta">${t.id}</span>
        </div>
        <span class="badge ${t.status.replace(/\s/g, "")}">${t.status}</span>
        <div class="card-actions">
          ${next ? `<button class="btn-small" data-status="${t.id}|${next}">Mark ${next}</button>` : ""}
        </div>
      </div>`;
  }).join("");

  el.querySelectorAll("[data-status]").forEach(btn =>
    btn.addEventListener("click", () => {
      const [tid, status] = btn.dataset.status.split("|");
      authority.updateTeamStatus(tid, status);
      renderAll();
    }));
}

function renderLog() {
  const el = document.getElementById("log");
  if (authority.log.length === 0) {
    el.innerHTML = `<p class="empty-note">Activity will appear here.</p>`;
    return;
  }
  el.innerHTML = authority.log.map(l => `
    <div class="log-entry">
      <div class="log-time">${l.time}</div>
      <div>${l.message}</div>
    </div>
  `).join("");
}

renderAll();