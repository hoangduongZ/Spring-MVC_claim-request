function addRow() {
    const table = document.getElementById('employeeTable');
    const rowCurrent = table.rows.length;
    const maxRows = employees.length; // Giới hạn số hàng theo số nhân viên

    if (rowCurrent - 1 >= maxRows) { // -1 để tính toán hàng tiêu đề
        alert("No employee free!");
        return;
    }

    const newRow = table.insertRow();
    newRow.classList.add("bg-white");
    newRow.innerHTML = `
        <td class=" px-4 py-2">
            <select name="employeeProjects[${rowCurrent - 1}].employeeId" class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                <option value="">-- Select Employee --</option>
                ${employees.map(emp => `<option value="${emp.employeeId}">${emp.accountName}</option>`).join('')}
            </select>
        </td>
        <td class=" px-4 py-2">
            <select name="employeeProjects[${rowCurrent - 1}].role" class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
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