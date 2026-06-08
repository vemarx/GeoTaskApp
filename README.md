# GeoTaskApp 📍

Aplicativo Android desenvolvido em Java para gerenciamento de tarefas com suporte a geolocalização, persistência local e integração com Firebase.

## 🚀 Funcionalidades

* Login com conta Google
* Autenticação utilizando Firebase Authentication
* Cadastro de tarefas
* Persistência local utilizando Room Database
* Armazenamento em nuvem utilizando Firebase Realtime Database
* Captura de localização do dispositivo
* Visualização de latitude e longitude
* Navegação entre telas Android

## 🛠️ Tecnologias Utilizadas

### Android

* Java
* Android Studio
* Android SDK

### Banco de Dados

* Room Database
* Firebase Realtime Database

### Autenticação

* Firebase Authentication
* Google Sign-In

### Localização

* Fused Location Provider API

## 📱 Estrutura do Projeto

```text
GeoTaskApp
│
├── LoginActivity
├── MainActivity
├── LocalizacaoActivity
│
├── Room Database
│   ├── AppDatabase
│   ├── Task
│   └── TaskDao
│
├── Firebase
│   ├── Authentication
│   └── Realtime Database
│
└── Geolocalização
```

## 🎯 Objetivo

O projeto foi desenvolvido para demonstrar a integração entre recursos modernos do ecossistema Android, incluindo autenticação de usuários, persistência local de dados, armazenamento em nuvem e serviços de localização.

## 📸 Funcionalidades Demonstradas

* Autenticação de usuários via Google
* Persistência híbrida (local e nuvem)
* Integração com APIs de localização
* Arquitetura utilizando Room
* Integração com Firebase

## 🔧 Como Executar

1. Clone o repositório:

```bash
git clone https://github.com/SEU-USUARIO/GeoTaskApp.git
```

2. Abra o projeto no Android Studio.

3. Configure o Firebase:

   * Crie um projeto no Firebase Console
   * Adicione o arquivo `google-services.json`

4. Execute o aplicativo em um dispositivo Android ou emulador.

## 👩‍💻 Autora

Desenvolvido por Verônica Palevicius
R.A.: R.A.: 1051392421008