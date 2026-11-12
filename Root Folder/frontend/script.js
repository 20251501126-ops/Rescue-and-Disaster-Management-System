/*
  RESCUE DISPATCH - BACKEND CONNECTED VERSION
  Spring Boot Backend: http://localhost:8081
*/

const CONFIG = {
    API_BASE: "http://localhost:8081/api"
};

const CLOSED = ["Completed", "Rejected"];

const $ = id => document.getElementById(id);

const esc = s => String(s ?? "").replace(/[&<>"']/g, c => ({
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#39;"
}[c]));

const clock = value => {
    if (!value) return "Time unavailable";
    const date = new Date(value);
    return Number.isNaN(date.getTime()) ? "Time unavailable" : date.toLocaleTimeString();
};

const ago = value => {
    if (!value) return "Time unavailable";
    const minutes = Math.floor((Date.now() - value) / 60000);
    return minutes < 1 ? "just now" :
        minutes < 60 ? `${minutes}m ago` :
        `${Math.floor(minutes / 60)}h ${minutes % 60}m ago`;
};

/* ---------- Application state ---------- */

const authority = {
    emergencies: [],
    teams: [],
    log: []
};

const filters = {
    q: "",
    status: "active",
    severity: "all"
};

let trackedId = "";
let dashboard = null;

/* ---------- Notifications ---------- */

function toast(message, type = "ok") {
    const container = $("toasts");
    if (!container) return;

    const item = document.createElement("div");
    item.className = "toast " + type;
    item.textContent = message;

    container.append(item);
    setTimeout(() => item.remove(), 4000);
}

function addLog(message) {
    authority.log.unshift({
        at: Date.now(),
        message
    });

    authority.log = authority.log.slice(0, 100);
    renderLog();
}

/* ---------- Backend API helper ---------- */

async function api(path, options = {}) {
    let response;

    try {
        response = await fetch(CONFIG.API_BASE + path, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                ...(options.headers || {})
            }
        });
    } catch (error) {
        throw new Error(
            "Could not reach the backend. Start Spring Boot on port 8081 and open this page from the local web server."
        );
    }

    const text = await response.text();
    let data = null;

    if (text) {
        try {
            data = JSON.parse(text);
        } catch {
            data = text;
        }
    }

    if (!response.ok) {
        const message =
            data?.message ||
            (data?.messages ? Object.values(data.messages).join(" ") : "") ||
            data?.error ||
            `Request failed (${response.status})`;

        throw new Error(message);
    }

    return data;
}

async function getEmergencies() {
    const data = await api("/emergencies");
    authority.emergencies = Array.isArray(data) ? data.map(mapEmergency) : [];
}

async function getTeams() {
    const data = await api("/teams");
    authority.teams = Array.isArray(data) ? data.map(mapTeam) : [];
}

async function getDashboard() {
    dashboard = await api("/dashboard/summary");
}

/* ---------- Backend response mapping ---------- */

function mapEmergency(e) {
    return {
        id: e.emergencyId,
        type: e.type,
        location: e.location,
        severity: e.severity,
        peopleAffected: e.peopleAffected,
        reportedBy: e.reportedBy,
        status: e.status,
        assignedTeam: e.assignedTeamId || null,
        createdAt: e.createdAt ? new Date(e.createdAt).getTime() : null
    };
}

function mapTeam(t) {
    return {
        id: t.teamId,
        name: t.teamName,
        status: t.teamStatus,
        currentLocation: t.currentLocation
    };
}

/* ---------- Refresh all backend data ---------- */

async function refreshAll(showMessage = false) {
    try {
        await Promise.all([
            getEmergencies(),
            getTeams(),
            getDashboard()
        ]);

        const connection = $("apiConnection");
        if (connection) {
            connection.classList.remove("offline");
            if (connection.lastChild) {
                connection.lastChild.textContent = " Connected to response system";
            }
        }

        renderAll();

        if (showMessage) {
            toast("Data successfully refreshed");
        }
    } catch (error) {
        const connection = $("apiConnection");
        if (connection) {
            connection.classList.add("offline");
            if (connection.lastChild) {
                connection.lastChild.textContent = " Response system unavailable";
            }
        }
        toast(error.message, "err");
    }
}

/* ---------- Domain helpers ---------- */

function findEmergency(id) {
    return authority.emergencies.find(e => e.id === id);
}

function findTeam(id) {
    return authority.teams.find(t => t.id === id);
}

function currentTeamEmergency(teamId) {
    return authority.emergencies.find(e =>
        e.assignedTeam === teamId &&
        ["Assigned", "En Route", "In Action"].includes(e.status)
    );
}

function freeTeamCount() {
    return authority.teams.filter(t => t.status === "Available").length;
}

/* ---------- Tabs ---------- */

const tabs = [...document.querySelectorAll(".tab")];

function showTab(name, focus = false) {
    document.body.classList.toggle("citizen-mode", name === "citizen");

    tabs.forEach(tab => {
        const active = tab.dataset.tab === name;

        tab.classList.toggle("active", active);
        tab.setAttribute("aria-selected", active);
        tab.tabIndex = active ? 0 : -1;

        if (active && focus) tab.focus();
    });

    document.querySelectorAll(".panel").forEach(panel => {
        panel.classList.toggle("hidden", panel.dataset.panel !== name);
    });

    history.replaceState(null, "", "#" + name);
}

tabs.forEach((tab, index) => {
    tab.addEventListener("click", () => showTab(tab.dataset.tab));

    tab.addEventListener("keydown", event => {
        const step = {
            ArrowRight: 1,
            ArrowLeft: -1
        }[event.key];

        if (step) {
            showTab(
                tabs[(index + step + tabs.length) % tabs.length].dataset.tab,
                true
            );
        }
    });
});

/* ---------- Citizen: report emergency ---------- */

function formError(message, field) {
    const status = $("citizenStatus");

    status.textContent = message;
    status.classList.add("error");

    if (field) {
        field.setAttribute("aria-invalid", "true");
        field.focus();
    }
}

$("reportForm").addEventListener("submit", async event => {
    event.preventDefault();

    const nameEl = $("citizenName");
    const phoneEl = $("citizenPhone");
    const locationEl = $("location");
    const affectedEl = $("affected");

    [nameEl, phoneEl, locationEl, affectedEl].forEach(field => {
        field.removeAttribute("aria-invalid");
    });

    $("citizenStatus").classList.remove("error");

    const name = nameEl.value.trim();
    const contactNumber = phoneEl.value.trim();
    const location = locationEl.value.trim();
    const peopleAffected = Number(affectedEl.value);

    if (name.length < 2) {
        return formError("Enter your name (at least 2 characters).", nameEl);
    }

    if (!/^[6-9]\d{9}$/.test(contactNumber)) {
        return formError("Enter a valid 10-digit Indian mobile number.", phoneEl);
    }

    if (!location) {
        return formError("Enter the emergency location.", locationEl);
    }

    if (!Number.isInteger(peopleAffected) ||
        peopleAffected < 1 ||
        peopleAffected > 10000) {
        return formError("People affected must be between 1 and 10,000.", affectedEl);
    }

    const submitButton = event.submitter;
    if (submitButton) submitButton.disabled = true;

    try {
        // STEP 1: Register citizen in the database
        const citizen = await api("/citizens", {
            method: "POST",
            body: JSON.stringify({
                name: name,
                contactNumber: contactNumber,
                address: location
            })
        });

        if (!citizen || !citizen.citizenId) {
            throw new Error("Citizen ID was not returned by backend.");
        }

        // STEP 2: Report emergency using generated Citizen ID
        const emergencyBody = {
            type: $("type").value,
            location: location,
            severity: $("severity").value,
            peopleAffected: peopleAffected,
            reportedBy: citizen.citizenId
        };

        const emergency = await api("/emergencies", {
            method: "POST",
            body: JSON.stringify(emergencyBody)
        });

        const id = emergency.emergencyId;

        $("citizenStatus").textContent =
            `Report ${id} submitted. Citizen ID: ${citizen.citizenId}. Keep your report ID to track it.`;

        toast(`Report ${id} successfully submitted`);

        trackedId = id;
        $("trackId").value = id;

        event.target.reset();

        $("severity").value = "Medium";
        $("affected").value = 1;

        addLog(`${name} (${contactNumber}) reported ${emergencyBody.type} at ${location}`);

        await refreshAll();
        renderTracker();

    } catch (error) {
        formError(error.message);
        toast(error.message, "err");

    } finally {
        if (submitButton) submitButton.disabled = false;
    }
});

/* ---------- Current location ---------- */

$("geoBtn").addEventListener("click", () => {
    if (!navigator.geolocation) {
        return toast("Location is not supported in this browser", "err");
    }

    navigator.geolocation.getCurrentPosition(
        position => {
            $("location").value =
                `${position.coords.latitude.toFixed(4)}, ${position.coords.longitude.toFixed(4)}`;
        },
        () => toast("Could not get location. Enter it manually.", "err"),
        { timeout: 8000 }
    );
});

/* ---------- Citizen: track emergency ---------- */

$("trackForm").addEventListener("submit", async event => {
    event.preventDefault();

    trackedId = $("trackId").value.trim().toUpperCase();
    if (!trackedId) {
        $("trackResult").innerHTML =
            `<p class="empty-note">Enter the report ID you received after submitting.</p>`;
        $("trackId").focus();
        return;
    }

    const button = event.submitter;
    if (button) button.disabled = true;

    $("trackResult").innerHTML =
        `<p class="empty-note">Checking the response system…</p>`;

    try {
        await getEmergencies();
        renderTracker();
    } catch (error) {
        $("trackResult").innerHTML =
            `<p class="empty-note">${esc(error.message)}</p>`;
    } finally {
        if (button) button.disabled = false;
    }
});

function renderTracker() {
    const element = $("trackResult");

    if (!trackedId) {
        element.innerHTML = "";
        return;
    }

    const emergency = findEmergency(trackedId);

    if (!emergency) {
        element.innerHTML = `
            <p class="empty-note">
                No report found with ID ${esc(trackedId)}.
                Check the ID and try again.
            </p>`;
        return;
    }

    const team = emergency.assignedTeam
        ? findTeam(emergency.assignedTeam)
        : null;

    element.innerHTML = `
        <p class="card-meta">
            ${esc(emergency.id)} — ${esc(emergency.type)}
            at ${esc(emergency.location)}
            ${team ? ` · ${esc(team.name)}` : ""}
        </p>
        <p>
            Current status:
            <strong>${esc(emergency.status)}</strong>
        </p>
        <p class="card-meta">
            Reported by: ${esc(emergency.reportedBy)}
        </p>
    `;
}

/* ---------- Authority filters ---------- */

$("filterQ").addEventListener("input", event => {
    filters.q = event.target.value.trim().toLowerCase();
    renderAuthority();
});

$("filterStatus").addEventListener("change", event => {
    filters.status = event.target.value;
    renderAuthority();
});

$("filterSeverity").addEventListener("change", event => {
    filters.severity = event.target.value;
    renderAuthority();
});

const priority = emergency =>
    ({ High: 3, Medium: 2, Low: 1 }[emergency.severity] || 0) * 1000 +
    Math.min(emergency.peopleAffected || 0, 999);

function visibleEmergencies() {
    const { q, status, severity } = filters;

    return authority.emergencies
        .filter(e =>
            !q ||
            `${e.id} ${e.type} ${e.location} ${e.reportedBy}`
                .toLowerCase().includes(q)
        )
        .filter(e =>
            status === "all" ||
            (status === "active"
                ? !CLOSED.includes(e.status)
                : e.status === status)
        )
        .filter(e => severity === "all" || e.severity === severity)
        .sort((a, b) =>
            Number(CLOSED.includes(a.status)) - Number(CLOSED.includes(b.status)) ||
            priority(b) - priority(a)
        );
}

/* ---------- Verify emergency ---------- */

async function verifyEmergency(id) {
    try {
        await api(`/emergencies/${encodeURIComponent(id)}/verify`, {
            method: "PUT"
        });

        addLog(`${id} verified by authority`);
        toast(`${id} verified`);

        await refreshAll();
    } catch (error) {
        toast(error.message, "err");
        await refreshAll();
    }
}

/* ---------- Reject emergency ---------- */

async function rejectEmergency(emergencyId) {
    try {
        await api(
            `/emergencies/${encodeURIComponent(emergencyId)}/reject`,
            { method: "PUT" }
        );

        addLog(`${emergencyId} rejected by authority`);
        toast(`${emergencyId} rejected successfully`);

        await refreshAll();
    } catch (error) {
        toast(error.message, "err");
        await refreshAll();
    }
}

/* ---------- Assign rescue team ---------- */

async function assignTeam(emergencyId, teamId) {
    try {
        await api(
            `/emergencies/${encodeURIComponent(emergencyId)}/assign?teamId=${encodeURIComponent(teamId)}`,
            { method: "PUT" }
        );

        addLog(`${teamId} assigned to ${emergencyId}`);
        toast(`${teamId} assigned successfully`);

        await refreshAll();
    } catch (error) {
        toast(error.message, "err");
        await refreshAll();
    }
}

/* ---------- Update team status ---------- */

async function updateTeamStatus(teamId, status) {
    try {
        const query = encodeURIComponent(status);

        await api(
            `/teams/${encodeURIComponent(teamId)}/status?status=${query}`,
            { method: "PUT" }
        );

        addLog(`${teamId} status changed to ${status}`);
        toast(`${teamId}: ${status}`);

        await refreshAll();
    } catch (error) {
        toast(error.message, "err");
        await refreshAll();
    }
}

/* ---------- Complete emergency ---------- */

async function completeEmergency(emergencyId) {
    try {
        await api(
            `/emergencies/${encodeURIComponent(emergencyId)}/complete`,
            { method: "PUT" }
        );

        addLog(`${emergencyId} completed`);
        toast(`${emergencyId} completed successfully`);

        await refreshAll();
    } catch (error) {
        toast(error.message, "err");
        await refreshAll();
    }
}

/* ---------- Action button handling ---------- */

document.addEventListener("click", async event => {
    const button = event.target.closest(
        "[data-verify],[data-reject],[data-assign],[data-status]"
    );

    if (!button) return;

    const data = button.dataset;
    button.disabled = true;

    try {
        if (data.verify) {
            await verifyEmergency(data.verify);
        }

        if (data.assign) {
            const [emergencyId, teamId] = data.assign.split("|");
            await assignTeam(emergencyId, teamId);
        }

        if (data.status) {
            const [teamId, status] = data.status.split("|");

            if (status === "Completed") {
                const emergency = currentTeamEmergency(teamId);

                if (!emergency) {
                    toast("No active emergency found for this team.", "err");
                    return;
                }

                await completeEmergency(emergency.id);
            } else {
                await updateTeamStatus(teamId, status);
            }
        }

        if (data.reject) {
            await rejectEmergency(data.reject);
        }

    } finally {
        button.disabled = false;
    }
});

/* ---------- CSV export ---------- */

$("exportBtn").addEventListener("click", () => {
    const rows = [
        [
            "ID",
            "Type",
            "Location",
            "Severity",
            "People",
            "Reported by",
            "Status",
            "Team"
        ]
    ];

    authority.emergencies.forEach(e => {
        rows.push([
            e.id,
            e.type,
            e.location,
            e.severity,
            e.peopleAffected,
            e.reportedBy,
            e.status,
            e.assignedTeam || ""
        ]);
    });

    const csv = rows
        .map(row => row.map(value =>
            `"${String(value ?? "").replace(/"/g, '""')}"`
        ).join(","))
        .join("\n");

    const blob = new Blob([csv], { type: "text/csv" });
    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = "emergencies.csv";
    link.click();

    URL.revokeObjectURL(url);
});

/* ---------- Dashboard ---------- */

function renderStats() {
    if (dashboard) {
        const active =
            (dashboard.totalEmergencies || 0) -
            (dashboard.completedEmergencies || 0) -
            (dashboard.rejectedEmergencies || 0);

        $("statActive").textContent = active;

        $("statCritical").textContent =
            authority.emergencies.filter(e =>
                e.severity === "High" &&
                !CLOSED.includes(e.status)
            ).length;

        $("statTeams").textContent =
            dashboard.availableRescueTeams ?? freeTeamCount();

        $("statDone").textContent =
            dashboard.completedEmergencies || 0;

    } else {
        $("statActive").textContent = 0;
        $("statCritical").textContent = 0;
        $("statTeams").textContent = 0;
        $("statDone").textContent = 0;
    }

    const waiting = authority.emergencies.filter(
        e => e.status === "Reported"
    ).length;

    $("queueCount").textContent = waiting;
    $("queueCount").hidden = waiting === 0;
}

/* ---------- Authority queue rendering ---------- */

function renderAuthority() {
    const element = $("authorityQueue");
    const list = visibleEmergencies();

    if (list.length === 0) {
        element.innerHTML = `
            <p class="empty-note">
                No emergencies found.
            </p>`;
        return;
    }

    element.innerHTML = list.map(e => `
        <div class="card sev-${esc(e.severity)}
            ${CLOSED.includes(e.status) ? "closed" : ""}">

            <div class="card-main">
                <span class="card-title">
                    ${esc(e.id)} — ${esc(e.type)} at ${esc(e.location)}
                </span>

                <span class="card-meta">
                    ${esc(e.peopleAffected)} affected ·
                    reported by ${esc(e.reportedBy)}
                    ${e.assignedTeam ? ` · Team ${esc(e.assignedTeam)}` : ""}
                </span>
            </div>

            <span class="badge ${esc(e.severity)}">
                ${esc(e.severity)}
            </span>

            <span class="badge ${esc(e.status.replace(/\s/g, ""))}">
                ${esc(e.status)}
            </span>

            <div class="card-actions">
                ${
                    e.status === "Reported"
                        ? `<button class="btn-small"
                            data-verify="${esc(e.id)}">Verify</button>`
                        : ""
                }

                ${
                    e.status === "Verified"
                        ? teamAssignButtons(e.id)
                        : ""
                }

                ${
                    ["Reported", "Verified"].includes(e.status)
                        ? `<button class="btn-small btn-danger"
                            data-reject="${esc(e.id)}">Reject</button>`
                        : ""
                }
            </div>
        </div>
    `).join("");
}

/* ---------- Available team assignment buttons ---------- */

function teamAssignButtons(emergencyId) {
    const free = authority.teams.filter(
        team => team.status === "Available"
    );

    if (free.length === 0) {
        return `<span class="empty-note">No teams available</span>`;
    }

    return free.map(team => `
        <button class="btn-small"
            data-assign="${esc(emergencyId)}|${esc(team.id)}">
            Assign ${esc(team.name)}
        </button>
    `).join("");
}

/* ---------- Team rendering ---------- */

function renderTeams() {
    const element = $("teamList");

    if (authority.teams.length === 0) {
        element.innerHTML =
            `<p class="empty-note">No rescue teams found.</p>`;
        return;
    }

    element.innerHTML = authority.teams.map(team => {
        let next = null;

        if (team.status === "En Route") next = "In Action";
        if (team.status === "In Action") next = "Completed";

        const job = currentTeamEmergency(team.id);

        return `
            <div class="card">
                <div class="card-main">
                    <span class="card-title">
                        ${esc(team.name)}
                    </span>

                    <span class="card-meta">
                        ${esc(team.id)}
                        ${job
                            ? ` · ${esc(job.id)}: ${esc(job.type)} at ${esc(job.location)}`
                            : " · standing by"}
                    </span>
                </div>

                <span class="badge ${esc(team.status.replace(/\s/g, ""))}">
                    ${esc(team.status)}
                </span>

                <div class="card-actions">
                    ${
                        next
                            ? `<button class="btn-small"
                                data-status="${esc(team.id)}|${next}">
                                Mark ${next}
                            </button>`
                            : ""
                    }
                </div>
            </div>
        `;
    }).join("");
}

/* ---------- Activity log ---------- */

function renderLog() {
    $("log").innerHTML = authority.log.length === 0
        ? `<p class="empty-note">Activity will appear here.</p>`
        : authority.log.map(item => `
            <div class="log-entry">
                <div class="log-time">${clock(item.at)}</div>
                <div>${esc(item.message)}</div>
            </div>
        `).join("");
}

/* ---------- Render everything ---------- */

function renderAll() {
    renderStats();
    renderAuthority();
    renderTeams();
    renderLog();
    renderTracker();
}

/* ---------- Initial application start ---------- */

const startTab = location.hash.slice(1);
const validTabs = ["citizen", "authority", "team"];

showTab(validTabs.includes(startTab) ? startTab : "citizen");

refreshAll();

/* Refresh data periodically so multiple users see updates. */
setInterval(() => refreshAll(), 30000);