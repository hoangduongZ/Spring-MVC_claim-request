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

// Claim create
function addRow() {
    const tableBody = document.getElementById("claimDetailTable").getElementsByTagName('tbody')[0];
    const newRow = tableBody.insertRow();
    newRow.innerHTML = `
            <td class="p-4">
                <input type="datetime-local" name="claimDetails[${tableBody.rows.length - 1}].startTime"
                       onchange="calculateDuration()"
                       class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
            </td>
            <td class="p-4">
                <input type="datetime-local" name="claimDetails[${tableBody.rows.length - 1}].endTime"
                       onchange="calculateDuration()"
                       class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
            </td>
            <td class="border px-4 py-2 text-center">
                <button type="button" class="bg-red-500 hover:bg-red-600 text-white py-1 px-2 rounded" onclick="removeRow(this)">Remove</button>
            </td>`;
}

function removeRow(button) {
    const row = button.parentNode.parentNode;
    row.parentNode.removeChild(row);
    calculateDuration();
}

function calculateDuration() {
    const projectStartDate = new Date(document.getElementById('projectStartDate').value);
    const projectEndDate = new Date(document.getElementById('projectEndDate').value);
    const rows = document.querySelectorAll("#claimDetailTable tbody tr");
    let totalDuration = 0;

    rows.forEach(row => {
        const startTimeInput = row.querySelector('input[name^="claimDetails"][name$="startTime"]');
        const endTimeInput = row.querySelector('input[name^="claimDetails"][name$="endTime"]');

        const startTime = new Date(startTimeInput.value);
        const endTime = new Date(endTimeInput.value);

        if (startTime < projectStartDate || endTime > projectEndDate) {
            alert("Start Time and End Time must be within the project's time frame.");
            startTimeInput.value = "";
            endTimeInput.value = "";
            return;
        }

        if (endTime <= startTime) {
            alert("End Time must be greater than Start Time.");
            endTimeInput.value = "";
            return;
        }

        if (startTime && endTime) {
            const duration = (endTime - startTime) / (1000 * 60 * 60);
            totalDuration += duration;
        }
    });

    document.getElementById('duration').value = totalDuration.toFixed(2);
}


