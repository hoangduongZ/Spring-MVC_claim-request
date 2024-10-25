function postRequest(url) {
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(() => {
            location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
        });
}