// Initialize map centered on Turkey
const map = L.map('map').setView([39.0, 35.0], 6);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors',
    maxZoom: 18
}).addTo(map);

const earthquakeLayer = L.layerGroup().addTo(map);
const helpRequestLayer = L.layerGroup().addTo(map);

function getMagnitudeColor(magnitude) {
    if (magnitude >= 6) return '#d32f2f';
    if (magnitude >= 5) return '#f57c00';
    if (magnitude >= 4) return '#fbc02d';
    if (magnitude >= 3) return '#388e3c';
    return '#1976d2';
}

function getMagnitudeRadius(magnitude) {
    return Math.max(magnitude * 4, 6);
}

function formatDate(isoString) {
    if (!isoString) return '-';
    const d = new Date(isoString);
    return d.toLocaleString('tr-TR', { timeZone: 'Europe/Istanbul' });
}

function loadEarthquakes() {
    const hours = document.getElementById('timeRange').value;
    fetch(`/api/v1/earthquakes/recent?hours=${hours}`)
        .then(res => res.json())
        .then(data => {
            earthquakeLayer.clearLayers();
            data.forEach(eq => {
                const circle = L.circleMarker([eq.latitude, eq.longitude], {
                    radius: getMagnitudeRadius(eq.magnitude),
                    fillColor: getMagnitudeColor(eq.magnitude),
                    color: '#333',
                    weight: 1,
                    opacity: 0.8,
                    fillOpacity: 0.6
                });

                circle.bindPopup(`
                    <div class="earthquake-popup">
                        <strong>${eq.title}</strong>
                        Büyüklük: ${eq.magnitude.toFixed(1)}<br>
                        Derinlik: ${eq.depthKm.toFixed(1)} km<br>
                        Zaman: ${formatDate(eq.eventTime)}<br>
                        ${eq.closestCity ? 'En yakın şehir: ' + eq.closestCity : ''}
                    </div>
                `);

                earthquakeLayer.addLayer(circle);
            });
            document.getElementById('earthquakeCount').textContent = `Deprem: ${data.length}`;
        })
        .catch(err => console.error('Deprem verisi yüklenemedi:', err));
}

// Toggle layers
document.getElementById('showEarthquakes').addEventListener('change', function() {
    if (this.checked) {
        map.addLayer(earthquakeLayer);
    } else {
        map.removeLayer(earthquakeLayer);
    }
});

document.getElementById('showHelpRequests').addEventListener('change', function() {
    if (this.checked) {
        map.addLayer(helpRequestLayer);
    } else {
        map.removeLayer(helpRequestLayer);
    }
});

document.getElementById('timeRange').addEventListener('change', loadEarthquakes);
document.getElementById('refreshBtn').addEventListener('click', function() {
    loadEarthquakes();
    loadHelpRequests();
});

// Initial load
loadEarthquakes();

// Auto-refresh every 60 seconds
setInterval(loadEarthquakes, 60000);
