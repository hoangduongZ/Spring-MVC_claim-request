let currentChart;

function destroyCurrentChart() {
    if (currentChart) {
        currentChart.destroy();
    }
}

function fetchReportStatus() {
    fetch('/api/claims/report/status')
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(rateMap => {
            const labels = Object.keys(rateMap);
            const data = Object.values(rateMap);
            const ctx = document.getElementById('reportChart').getContext('2d');

            destroyCurrentChart();

            currentChart = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Approval vs Rejection Rate',
                        data: data,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(75, 192, 192, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(75, 192, 192, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: false,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'top',
                        }
                    }
                }
            });
        })
        .catch(error => console.error("Error fetching report status:", error));
}

function fetchProjectReport() {
    fetch('/api/claims/report/project')
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            const ctx = document.getElementById('reportChart').getContext('2d');

            destroyCurrentChart();

            currentChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: Object.keys(data),
                    datasets: [{
                        label: 'Claims per Project',
                        data: Object.values(data),
                        backgroundColor: '#9bb53f'
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top',
                        }
                    }
                }
            });
        })
        .catch(error => console.error("Error fetching project report:", error));
}

function fetchByDate() {
    fetch('/api/claims/report/by-date')
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            const labels = Object.keys(data);
            const counts = Object.values(data);
            const ctx = document.getElementById('reportChart').getContext('2d');

            destroyCurrentChart();

            currentChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Claims Created',
                        data: counts,
                        borderColor: 'rgba(75, 192, 192, 1)',
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        fill: true,
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        x: {
                            title: {
                                display: true,
                                text: 'Date'
                            }
                        },
                        y: {
                            title: {
                                display: true,
                                text: 'Number of Claims'
                            },
                            beginAtZero: true
                        }
                    }
                }
            });
        })
        .catch(error => console.error('Error fetching claim data:', error));
}

// fetchReportStatus();
