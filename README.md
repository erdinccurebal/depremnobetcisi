# Deprem Nobetcisi

Turkiye'deki depremleri izleyen, kullanicilara Telegram uzerinden yakinlarindaki depremleri bildiren ve yardim taleplerini herkese acik harita uzerinde gosteren bir uygulamadir.

## Ozellikler

- **Deprem Izleme**: Kandilli Rasathanesi API'si uzerinden 30 saniyede bir deprem verisi sorgulanir
- **Telegram Bildirimleri**: Kayitli kullanicilara yakinlarindaki depremler icin otomatik bildirim gonderilir
- **Yardim Talepleri**: Kullanicilar Telegram bot uzerinden tek tusla yardim talebi olusturabilir
- **Canli Harita**: Yardim talepleri ve depremler herkese acik harita uzerinde gosterilir
- **KVKK Uyumlu**: Kullanici verileri KVKK onayi ile alinir, istendiginde tamamen silinir

## Teknoloji Yigini

- Java 21, Spring Boot 3.4.3, Maven
- PostgreSQL 16 + Flyway migration
- TelegramBots 9.x (Long Polling)
- Thymeleaf + Leaflet.js (harita arayuzu)
- Hexagonal Architecture (Ports & Adapters)

## Mimari

```
com.depremnobetcisi/
  domain/                  # Is mantigi (framework bagimsiz)
    model/                 # POJO'lar, enum'lar, deger nesneleri
    port/input/            # Kullanim senaryosu arayuzleri
    port/output/           # Repository ve dis servis arayuzleri
    service/               # Is mantigi uygulamalari
  infrastructure/          # Dis dunya baglantilari
    input/telegram/        # Bot, konusma yonetimi, klavyeler
    input/web/             # REST controller'lar, Thymeleaf
    input/scheduler/       # Deprem sorgulama zamanlayicisi
    output/persistence/    # JPA entity, repository, adapter
    output/api/            # Kandilli API istemcisi
    output/notification/   # Telegram bildirim gonderici
    config/                # Spring yapilandirmalari
```

## Hizli Baslangic

### Gereksinimler
- Java 21
- Maven 3.9+
- Docker

### Kurulum

```bash
# Repoyu klonla
git clone https://github.com/your-username/depremnobetcisi.git
cd depremnobetcisi

# PostgreSQL'i baslat
docker compose up -d postgres

# Ortam degiskenlerini ayarla (.env.example'i referans alin)
export TELEGRAM_BOT_TOKEN=your-bot-token
export TELEGRAM_ENABLED=true

# Uygulamayi calistir
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Uygulama `http://localhost:8080` adresinde harita arayuzu ile baslar.

### Telegram Bot Olusturma

1. Telegram'da [@BotFather](https://t.me/BotFather) ile konusun
2. `/newbot` komutu ile yeni bot olusturun
3. Aldigniz token'i `TELEGRAM_BOT_TOKEN` ortam degiskeni olarak ayarlayin

### Docker ile Calistirma

```bash
# Uygulamayi derle
mvn clean package -DskipTests

# Docker ile calistir
docker compose up -d
```

## Ortam Degiskenleri

| Degisken | Aciklama | Varsayilan |
|----------|----------|------------|
| `DATABASE_URL` | PostgreSQL baglanti URL'i | `jdbc:postgresql://localhost:5432/depremnobetcisi` |
| `DATABASE_USERNAME` | Veritabani kullanici adi | `postgres` |
| `DATABASE_PASSWORD` | Veritabani sifresi | `postgres` |
| `TELEGRAM_BOT_TOKEN` | Telegram bot token | - |
| `TELEGRAM_ENABLED` | Bot'u aktif/pasif yap | `false` |

## Testler

```bash
# Tum testler (Docker gerekli - Testcontainers)
mvn verify

# Sadece birim testleri
mvn test
```

Test turleri:
- **Birim testleri**: Domain servisleri (Mockito)
- **Entegrasyon testleri**: Repository katmani (Testcontainers + PostgreSQL)
- **API testleri**: Kandilli API istemcisi (WireMock)
- **Mimari testleri**: Hexagonal architecture kurallari (ArchUnit)

## API Endpointleri

| Endpoint | Aciklama |
|----------|----------|
| `GET /` | Harita arayuzu |
| `GET /api/v1/help-requests` | Aktif yardim talepleri |
| `GET /api/v1/help-requests/nearby?lat=&lng=&radius=` | Yakin yardim talepleri |
| `GET /api/v1/earthquakes/recent?hours=24` | Son depremler |

## Telegram Bot Komutlari

| Buton / Komut | Aciklama |
|---------------|----------|
| Bilgilerimi Goster | Kayitli profil bilgilerini gosterir |
| Bilgilerimi Guncelle | Konum, ad, telefon, adres ve KVKK onayi kaydeder |
| Yardim Iste | Kayitli bilgilerle yardim talebi olusturur |
| Yardim Istegini Sil | Aktif yardim talebini haritadan kaldirir |
| Beni Unut | Tum kullanici verilerini siler |
| `/iptal` | Devam eden islemi iptal eder |

## Katkida Bulunma

Katkida bulunmak icin [CONTRIBUTING.md](CONTRIBUTING.md) dosyasini inceleyin.

## Lisans

Bu proje [MIT Lisansi](LICENSE) ile lisanslanmistir.
