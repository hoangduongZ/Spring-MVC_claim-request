// function openModal() {
//     document.getElementById("errorModal").classList.remove("hidden");
// }

function closeErrModal() {
    document.getElementById("errorModal").classList.add("hidden");
}

function closeModal() {
    document.getElementById("actionModal").classList.add("hidden");
}

function openModal(actionUrl, title, message) {
    document.getElementById("modalForm").action = actionUrl;
    document.getElementById("modalTitle").innerText = title;
    document.getElementById("modalMessage").innerText = message;
    document.getElementById("actionModal").classList.remove("hidden");
}

function closeModal() {
    document.getElementById("actionModal").classList.add("hidden");
}

document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll("button[data-type]").forEach(button => {
        button.addEventListener("click", function(event) {
            event.preventDefault(); // Prevent immediate form submission
            const claimId = button.getAttribute("data-id");
            const actionType = button.getAttribute("data-type");
            const actionUrl = `/claims/${actionType}/${claimId}`;
            const reasonTextarea = document.getElementById("reason");
            const reasonBlock = document.getElementById("reason-block");

            let title = "";
            let message = "";
            if (actionType === "returned") {
                title = "Confirm Return";
                message = "Are you sure you want to return this claim? Please provide a reason:";
                reasonTextarea.setAttribute("name", "returnReason");
                reasonTextarea.removeAttribute("disabled");
                reasonBlock.classList.remove("hidden");
            } else if (actionType === "rejected") {
                title = "Confirm Reject";
                message = "Are you sure you want to reject this claim? Please provide a reason:";
                reasonTextarea.setAttribute("name", "rejectReason");
                reasonTextarea.removeAttribute("disabled");
                reasonBlock.classList.remove("hidden");
            } else {
                title = "Confirm Approve";
                message = "Are you sure you want to approve this claim?";
                reasonTextarea.removeAttribute("name");
                reasonTextarea.setAttribute("disabled", "true");
                reasonBlock.classList.add("hidden");
            }

            openModal(actionUrl, title, message);
        });
    });
});
