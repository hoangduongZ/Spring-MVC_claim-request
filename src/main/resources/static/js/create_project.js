const selectedEmployees = new Set();

function addRow() {
    const table = document.getElementById('employeeTable');
    const rowCurrent = table.rows.length;
    if (rowCurrent > employees.length){
        alert("Row exceed number of Employee !");
        return;
    }

    const rowNum = table.rows.length - 1;
    const newRow = table.insertRow();
    newRow.classList.add("bg-white");
    newRow.innerHTML = `
        <td class="border px-4 py-2">
            <select name="employeeProjects[${rowNum}].employeeId" class="border rounded py-1 px-2" onchange="updateSelectedEmployees(this.value)">
                <option value="">-- Select Employee --</option>
                ${employees.map(emp => `<option value="${emp.id}">${emp.userName}</option>`).join('')}
            </select>
        </td>
        <td class="border px-4 py-2">
            <select name="employeeProjects[${rowNum}].role" class="border rounded py-1 px-2">
                <option value="">-- Select Role --</option>
                ${roles.map(role => `<option value="${role}">${role}</option>`).join('')}
            </select>
        </td>
        <td class="border px-4 py-2 text-center">
            <button type="button" class="bg-red-500 hover:bg-red-600 text-white py-1 px-2 rounded" onclick="removeRow(this)">Remove</button>
        </td>
    `;
    refreshIndexRow();
}

function removeRow(button) {
    const row = button.parentNode.parentNode;
    row.parentNode.removeChild(row);
    refreshIndexRow();
}

function refreshIndexRow() {
    const table = document.getElementById('employeeTable');
    const rows = table.querySelectorAll('tbody tr');

    rows.forEach((row, index) => {
        const employeeSelect = row.querySelector('select[name*="employeeId"]');
        const roleSelect = row.querySelector('select[name*="role"]');

        employeeSelect.name = `employeeProjects[${index}].employeeId`;
        roleSelect.name = `employeeProjects[${index}].role`;
    });
}