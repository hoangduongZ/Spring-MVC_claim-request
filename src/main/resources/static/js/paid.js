function checkCheckboxes() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    const exportButton = document.getElementById('exportButton');
    let isChecked = false;

    checkboxes.forEach((checkbox) => {
        if (checkbox.checked) {
            isChecked = true;
        }
    });

    exportButton.style.display = isChecked ? 'inline-block' : 'none';
}

document.querySelectorAll('input[type="checkbox"]').forEach((checkbox) => {
    checkbox.addEventListener('change', checkCheckboxes);
});

function submitExportForm() {
    document.getElementById('exportForm').submit();
}
