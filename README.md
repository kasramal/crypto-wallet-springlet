# crypto-wallet-springlet

# 🚀 Cryptocurrency Management Platform

A modular **Java-based microservices architecture** platform that handles essential cryptocurrency functions, including
address creation, HD wallet management, transaction signing, and broadcasting to the blockchain network. The system is
built with Spring Boot microservices, Eureka service discovery, OAuth2 authentication, and an API gateway.

---

## 📚 Project Overview

This project is designed following a **microservices architecture**, where each component runs as an independent service
communicating through APIs. The key services include:

- **🗂️ Eureka Registry:** Service discovery server for dynamic registration and lookup of microservices.
- **🔐 OAuth2 Authentication Service:** Centralized authentication and authorization using OAuth2 protocol.
- **🚪 API Gateway:** Single entry point for client requests, routing traffic to appropriate microservices and enforcing
  security.
- **💼 Core Service:** Implements core cryptocurrency functions such as:
    - Generating cryptocurrency addresses
    - HD wallet (Hierarchical Deterministic wallets) management
    - Transaction creation and signing
    - Broadcasting transactions to blockchain networks
    - Balance inquiry support using multiple third-party API service providers

This architecture enables scalability, maintainability, and independent deployment of services.

---

## ✨ Features

- 🔄 Fully **microservices-based design** for scalability and resilience
- 🔍 Dynamic service discovery with Eureka
- 🔑 Secure OAuth2 authentication and authorization
- 🚪 API Gateway for unified client access
- 💰 Multi-cryptocurrency support
- 🔑 HD wallet key management
- 📝 Transaction signing and broadcasting

---

## 🛠️ Tech Stack

- ☕ Java 8 with Spring Boot microservices
- 📡 Spring Cloud Netflix Eureka
- 🔒 Spring Security OAuth2
- 🚪 Spring Cloud Gateway
- 🗄️ Hibernate ORM with JPA
- 🔗 RESTful APIs

---
## Supported Cryptocurrencies

The project currently supports the following cryptocurrencies, with their respective logos sized uniformly for clarity:

<table>
  <thead>
    <tr>
      <th>Cryptocurrency</th>
      <th>Symbol</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/bOabBYkcX/bitcoin_btc.svg" alt="Bitcoin" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;"></td>
      <td>Bitcoin</td>
      <td>BTC</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/388ehh6kq/bitcoin-sv-1.svg" alt="Bitcoin SV" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;">Bitcoin </td>
      <td>SV</td>
      <td>BSV</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/By8ziihX7/bch.svg" alt="Bitcoin Cash" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;">Bitcoin </td>
      <td>Cash</td>
      <td>BCH</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/BUvPxmc9o/ltcnew.svg" alt="Litecoin" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;"></td>
      <td>Litecoin</td>
      <td>LTC</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/B1oPuTyfX/xrp.svg" alt="Ripple" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;"></td>
      <td>Ripple</td>
      <td>XRP</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/PyCmduSxt/Dash-D-white_on_blue_circle.svg" alt="Dash" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;"></td>
      <td>Dash</td>
      <td>DASH</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/rJrKiS_uZ/zec.svg" alt="Zcash" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;"></td>
      <td>Zcash</td>
      <td>ZEC</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/H1arXIuOZ/doge.svg" alt="Dogecoin" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;"></td>
      <td>Dogecoin</td>
      <td>DOGE</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/mgHqwlCLj/usdt.svg" alt="Tether" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;"></td>
      <td>Tether</td>
      <td>USDT</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/rk4RKHOuW/eth.svg" alt="Ethereum" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;"></td>
      <td>Ethereum</td>
      <td>ETH</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/rJfyor__W/etc.svg" alt="Ethereum Classic" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;">Ethereum </td>
      <td>Classic</td>
      <td>ETC</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/behejNqQs/trx.svg" alt="Tron" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;"></td>
      <td>Tron</td>
      <td>TRX</td>
    </tr>
    <tr>
      <td style="width:20px;height:20px;transform: scale(0.5)"><img src="https://cdn.coinranking.com/HkLUdilQ7/xtz.svg" alt="Tezos" style="max-width:24px; max-height:24px; vertical-align:middle; margin-right:8px;"></td>
      <td>Tezos</td>
      <td>XTZ</td>
    </tr>
  </tbody>
</table>

---

## ⚡ Getting Started

### ✅ Prerequisites

- ☕ Java 8
- 🛠️ Maven or Gradle
- 🐳 Docker (optional)
- 🗄️ Supported relational database
- Enter your API-KEYs in application.yml of core-service in order to have access to third-party API providers (used to fetch current balance and push locally signed transactions into network)

### ▶️ Running the Project

1. Start Eureka Registry
2. Start OAuth2 Authentication Service
3. Start API Gateway
4. Start Core Service

Each service registers itself with Eureka and communicates through APIs.

---

## 🔒 Security

- OAuth2-based authentication and role-based access control
- Secure private key handling

---

## 📖 API Documentation

### 1. Authentication token

```curl
curl -v --location 'http://localhost:7000/api/authentication/oauth/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Authorization: Basic Y29yZS1zZXJ2aWNlOmNvcmUtc2VydmljZS1zZWNyZXQ=' \
--data-urlencode 'username=demohouse' \
--data-urlencode 'password=?0demoHouse*' \
--data-urlencode 'grant_type=password'
```

### 2. Generate Wallet Address
You can replace ETH with any other supported cryptocurrency symbol
```curl
curl --location 'http://localhost:7000/api/core-service/v1/wallet/ETH/generate' \
--header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTE1MDM4NTAsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiQURNSU4iXSwianRpIjoiNDQ4ZWEzODYtOTcwNi00ZmJmLTk2ZjAtZGI2ZjY5NGRhZGRiIiwiY2xpZW50X2lkIjoiY29yZS1zZXJ2aWNlIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.O_MTY3JrUBd4SVFAcZ-1tyzelni9W2A1o8HAjACJ83M'
```

### 3. Generate Mnemonic words
You can replace ETH with any other supported cryptocurrency symbol
```curl
curl --location 'http://localhost:7000/api/core-service/v1/hd-wallet/ETH/mnemonic' \
--header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTE1MDM4NTAsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiQURNSU4iXSwianRpIjoiNDQ4ZWEzODYtOTcwNi00ZmJmLTk2ZjAtZGI2ZjY5NGRhZGRiIiwiY2xpZW50X2lkIjoiY29yZS1zZXJ2aWNlIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.O_MTY3JrUBd4SVFAcZ-1tyzelni9W2A1o8HAjACJ83M'
```

### 4. Generate HD Wallet Address using Mnemonics
You can replace ETH with any other supported cryptocurrency symbol
```curl
curl --location 'http://localhost:7000/api/core-service/v1/hd-wallet/BTC/generate' \
--header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTE1MDM4NTAsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiQURNSU4iXSwianRpIjoiNDQ4ZWEzODYtOTcwNi00ZmJmLTk2ZjAtZGI2ZjY5NGRhZGRiIiwiY2xpZW50X2lkIjoiY29yZS1zZXJ2aWNlIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.O_MTY3JrUBd4SVFAcZ-1tyzelni9W2A1o8HAjACJ83M' \
--header 'Content-Type: application/json' \
--data '{
    "mnemonic": "neighborhoods heard forward rebellion almost fiction never volunteers pursuit ruined put controversial recycling enormous stipulated editing activities appreciates",
    "password": "123password"
}'
```

### 5. Retrieve HD Wallet Address using Mnemonics
You can replace ETH with any other supported cryptocurrency symbol
```curl
curl --location 'http://localhost:7000/api/core-service/v1/hd-wallet/BTC/retrieve' \
--header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTE1MDM4NTAsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiQURNSU4iXSwianRpIjoiNDQ4ZWEzODYtOTcwNi00ZmJmLTk2ZjAtZGI2ZjY5NGRhZGRiIiwiY2xpZW50X2lkIjoiY29yZS1zZXJ2aWNlIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.O_MTY3JrUBd4SVFAcZ-1tyzelni9W2A1o8HAjACJ83M' \
--header 'Content-Type: application/json' \
--data '{
    "mnemonic": "neighborhoods heard forward rebellion almost fiction never volunteers pursuit ruined put controversial recycling enormous stipulated editing activities appreciates",
    "password": "123password"
}'
```

### 6. Inquire current balance for HD Wallet Address using Mnemonics
You can replace ETH with any other supported cryptocurrency symbol
```curl
curl --location 'http://localhost:7000/api/core-service/v1/hd-wallet/BTC/balance' \
--header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTE1MDM4NTAsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiQURNSU4iXSwianRpIjoiNDQ4ZWEzODYtOTcwNi00ZmJmLTk2ZjAtZGI2ZjY5NGRhZGRiIiwiY2xpZW50X2lkIjoiY29yZS1zZXJ2aWNlIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.O_MTY3JrUBd4SVFAcZ-1tyzelni9W2A1o8HAjACJ83M' \
--header 'Content-Type: application/json' \
--data '{
    "mnemonic": "neighborhoods heard forward rebellion almost fiction never volunteers pursuit ruined put controversial recycling enormous stipulated editing activities appreciates",
    "password": "123password"
}'
```

### 7. Sign Transaction
You can replace ETH with any other supported cryptocurrency symbol
If source address is not generated by this service you need to provide privateKey in the request body as well!
```curl
curl --location 'http://localhost:7000/api/core-service/v1/wallet-transaction/ETH/sign' \
--header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTE1MDM4NTAsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiQURNSU4iXSwianRpIjoiNDQ4ZWEzODYtOTcwNi00ZmJmLTk2ZjAtZGI2ZjY5NGRhZGRiIiwiY2xpZW50X2lkIjoiY29yZS1zZXJ2aWNlIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.O_MTY3JrUBd4SVFAcZ-1tyzelni9W2A1o8HAjACJ83M' \
--header 'Content-Type: application/json' \
--data '{
    "from": "0x6e5308E2f41282336a68cE0eccbF6d1D1Ba82098",
    "to": "0x96303fD1C98d242794c8623C0A84A8bEf32669b2",
    "value": 0.001
}'
```

### 8. Sign and push Transaction
You can replace ETH with any other supported cryptocurrency symbol
If source address is not generated by this service you need to provide privateKey in the request body as well!
```curl
curl --location 'http://localhost:7000/api/core-service/v1/wallet-transaction/ETH/push/raw' \
--header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTE1MDM4NTAsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiQURNSU4iXSwianRpIjoiNDQ4ZWEzODYtOTcwNi00ZmJmLTk2ZjAtZGI2ZjY5NGRhZGRiIiwiY2xpZW50X2lkIjoiY29yZS1zZXJ2aWNlIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.O_MTY3JrUBd4SVFAcZ-1tyzelni9W2A1o8HAjACJ83M' \
--header 'Content-Type: application/json' \
--data '{
    "from": "0x6e5308E2f41282336a68cE0eccbF6d1D1Ba82098",
    "to": "0x96303fD1C98d242794c8623C0A84A8bEf32669b2",
    "value": 0.001
}'
```

### 9. Push a signed Transaction
You can replace ETH with any other supported cryptocurrency symbol
If source address is not generated by this service you need to provide privateKey in the request body as well!
```curl
curl --location 'http://localhost:7000/api/core-service/v1/wallet-transaction/ETH/push/hex' \
--header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTE1MDM4NTAsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiQURNSU4iXSwianRpIjoiNDQ4ZWEzODYtOTcwNi00ZmJmLTk2ZjAtZGI2ZjY5NGRhZGRiIiwiY2xpZW50X2lkIjoiY29yZS1zZXJ2aWNlIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.O_MTY3JrUBd4SVFAcZ-1tyzelni9W2A1o8HAjACJ83M' \
--header 'Content-Type: application/json' \
--data '{
    "transactionHex": "0x63..",
}'
```

---

## 🤝 Contributing

Contributions welcome via pull requests.

---