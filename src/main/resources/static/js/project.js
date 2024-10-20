function addRow() {
    const table = document.getElementById('employeeTable');
    const rowCurrent = table.rows.length;
    const maxRows = employees.length;

    if (rowCurrent - 1 >= maxRows) {
        alert("No employee free!");
        return;
    }

    const newRow = table.insertRow();
    newRow.classList.add("bg-white");
    newRow.innerHTML = `
       <td class="px-4 py-2">
    <select name="employeeProjects[${rowCurrent - 1}].employeeId" class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
        <option value="">-- Select Employee --</option>
        ${employees.map(emp => `<option value="${emp.employeeId}">${emp.accountName}</option>`).join('')}
    </select>
</td>

<td class="px-4 py-2">
    <select name="employeeProjects[${rowCurrent - 1}].role" class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
        <option value="">-- Select Role --</option>
        ${roles.map(role => `<option value="${role}">${role}</option>`).join('')}
    </select>
</td>

<td class="px-4 py-2">
    <input type="date" id="startJoin" name="employeeProjects[${rowCurrent - 1}].startDate" class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" >
</td>

<td class="px-4 py-2">
    <input type="date" id="endJoin" name="employeeProjects[${rowCurrent - 1}].endDate" class="block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
</td>

<td class="px-4 py-2 flex justify-center items-center">
    <button type="button" onclick="removeRow(this)" class="flex justify-center items-center w-8 h-8 text-xl font-bold text-white bg-red-500 shadow rounded-full hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-opacity-50">
        <i class="fa-solid fa-minus"></i>
    </button>
</td>`;
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
        const startDateInput = row.querySelector('input[name*="startDate"]');
        const endDateInput = row.querySelector('input[name*="endDate"]');

        employeeSelect.name = `employeeProjects[${index}].employeeId`;
        roleSelect.name = `employeeProjects[${index}].role`;
        startDateInput.name = `employeeProjects[${index}].startDate`;
        endDateInput.name = `employeeProjects[${index}].endDate`;
    });
}