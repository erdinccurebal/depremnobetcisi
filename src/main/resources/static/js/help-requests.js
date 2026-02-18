const helpIcon = L.icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png',
    shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});

function loadHelpRequests() {
    fetch('/api/v1/help-requests')
        .then(res => res.json())
        .then(data => {
            helpRequestLayer.clearLayers();
            data.forEach(hr => {
                const marker = L.marker([hr.latitude, hr.longitude], { icon: helpIcon });

                marker.bindPopup(`
                    <div class="help-popup">
                        <strong>🆘 Yardım Talebi</strong><br>
                        Ad: ${hr.fullName || 'Paylaşmamış'}<br>
                        Telefon: ${hr.phoneNumber || 'Paylaşmamış'}<br>
                        Konum: ${hr.latitude.toFixed(4)}, ${hr.longitude.toFixed(4)}<br>
                        Adres: ${hr.addressText || 'Paylaşmamış'}<br>
                        ${hr.description ? 'Açıklama: ' + hr.description + '<br>' : ''}
                        İlan Tarihi: ${formatDate(hr.createdAt)}
                    </div>
                `);

                helpRequestLayer.addLayer(marker);
            });
            document.getElementById('helpRequestCount').textContent = `Yardım Talebi: ${data.length}`;
        })
        .catch(err => console.error('Yardım talepleri yüklenemedi:', err));
}

// Initial load
loadHelpRequests();

// Auto-refresh every 60 seconds
setInterval(loadHelpRequests, 60000);
