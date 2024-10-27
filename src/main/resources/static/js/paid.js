function checkCheckboxes() {

}

document.querySelectorAll('input[type="checkbox"][name="claimIds"]').forEach((checkbox) => {
    checkbox.addEventListener('change', checkCheckboxes);
});

function submitExportForm() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"][name="claimIds"]');

    const isChecked = Array.from(checkboxes).some(checkbox => checkbox.checked);

    if (isChecked) {
        document.getElementById('exportForm').submit();
    } else {
        alert("Vui lòng chọn ít nhất một mục để xuất!");
    }
}

function toggleAllCheckboxes() {
    const isChecked = document.getElementById("selectAllCheckbox").checked;

    document.querySelectorAll("input[name='claimIds']").forEach(checkbox => {
        checkbox.checked = isChecked;
    });
    checkCheckboxes()
}

