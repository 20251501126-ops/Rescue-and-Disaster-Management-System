function showEmergencyForm() {
    document.getElementById("emergencyForm").style.display = "block";
}

function submitEmergency() {

    const location = document.getElementById("location").value;

    if (location === "") {
        alert("Please enter the emergency location.");
        return;
    }

    alert("Emergency submitted successfully!");
}