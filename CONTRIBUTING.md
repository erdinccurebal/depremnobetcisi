# Katkida Bulunma Rehberi

Deprem Nobetcisi projesine katkida bulunmak istediginiz icin tesekkurler!

## Gelistirme Ortami

### Gereksinimler
- Java 21
- Maven 3.9+
- Docker (PostgreSQL icin)

### Kurulum

```bash
# Repoyu klonla
git clone https://github.com/your-username/depremnobetcisi.git
cd depremnobetcisi

# PostgreSQL'i baslat
docker compose up -d postgres

# Uygulamayi calistir
TELEGRAM_ENABLED=false mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Testleri Calistirma

```bash
# Tum testler (Testcontainers icin Docker gerekli)
mvn verify

# Sadece birim testleri
mvn test -Dtest="*ServiceTest"
```

## Kod Standartlari

- Hexagonal Architecture kuralarina uyun (ArchUnit testleri bunu dogrular)
- Domain katmani Spring/JPA bagimliligina sahip olmamalidir
- Yeni ozellikler icin test yazin
- Turkce kullanici mesajlari, Ingilizce kod

## Pull Request Sureci

1. Yeni bir branch olusturun (`feature/ozellik-adi`)
2. Degisikliklerinizi yapin
3. Testlerin gectiginden emin olun (`mvn verify`)
4. PR olusturun ve degisiklikleri aciklayin

## Mimari

Proje Hexagonal Architecture (Ports & Adapters) kullanir:

```
domain/          -> Is mantigi (framework bagimsiz)
  model/         -> POJO'lar, enum'lar
  port/input/    -> Kullanim senaryosu arayuzleri
  port/output/   -> Repository arayuzleri
  service/       -> Is mantigi uygulamalari

infrastructure/  -> Dis dunya baglantilari
  input/         -> Telegram bot, Web controller, Scheduler
  output/        -> JPA, API client, Notification sender
  config/        -> Spring yapilandirmalari
```
