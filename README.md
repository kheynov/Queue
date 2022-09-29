# Бекенд для VK MiniApp приложения для менеджмента очередей

__Стек__: Kotlin, Ktor, MongoDB, Docker

---
Запуск сервера в Docker compose:

```shell
    docker-compose up --build -d
```

---
Пересборка сервера при необходимости:

```shell
./gradlew buildFatJar
```

---
Остановка сервера:

```shell
docker-compose down
```

---

# API

1) Пользователи

### Регистрация пользователя

```http request
POST /api/v1/user

body:
{
	"token": "324rb234fwf2345rf",
	"name": "Ivan"
}

Response: 200 OK / 409 Conflict
```

### Удаление пользователя

```http request
DELETE /api/v1/user

body:
{
	"token": "324rb234fwf2345rf"
}

Response: 200 OK / 409 Conflict
```

### Получение сведений о комнатах пользователя

```http request
GET /api/v1/user/rooms

body:
{
	"token": "324rb234fwf2345rf"
}

Response: 200 OK
body: 
[]
or
[
	{
		"id": 1,
		"name": "English 22.10",
		"user_ids": [
			92610861,
			879988134
		],
		"admin_ids": [
			92610861
		],
		"settings": null,
		"announcements": null,
		"queues": [
			{
				"id": 1,
				"name": "queue 1",
				"userIds": [
					879988134,
					92610861
				]
			}
		]
	}
]
```

_**Пароль от комнаты показывается только администратору (создателю комнаты)**_

2) Комнаты

### Создание комнаты

```http request
POST /api/v1/room

body:
{
	"token": "324rb234fwf2345rf"
	"roomName": "English 22.10",
	"password" : "123123"
}

Response: 201 Created / 409 Conflict
```

### Удаление комнаты

```http request
DELETE /api/v1/room

body:
{
	"token": "324rb234fwf2345rf"
	"roomId": 1
}

Response: 200 OK / 409 Conflict / ...
```

### Сведения об очередях комнаты

```http request
GET /api/v1/room/queues

body:
{
	"token": "324rb234fwf2345rf"
	"roomId": 1
}

Response: 204 No content
or 
200 OK
body: 
[
	{
		"id": 1,
		"name": "queue 1",
		"usersCount": 2
	}
]
```

### Добавления пользователя в комнату

```http request
POST /api/v1/room/join
body:
{
"token": "324rb234fwf2345rf",
"roomId": 1,
"password": "123123"
}

Response: 200 OK / 409 Conflict / ...
```

### Удаление пользователя из комнаты

```http request
DELETE /api/v1/room/leave
body:
{
"token": "324rb234fwf2345rf",
"roomId": 1
}

Response: 200 OK / ...
```

3) Очереди

### Создание очереди

```http request
POST /api/v1/queue
body:
{
	"token": "324rb234fwf2345rf",
	"roomId": 1,
	"name" : "queue 1"
}

Response: 200 OK / 409 Conflict / ...
```

### Удаление очереди (сделать это может только создатель очереди или админ комнаты)

```http request
DELETE /api/v1/queue
body:
{
	"token": "324rb234fwf2345rf",
	"roomId": 1,
	"queueId" : 1
}
Response: 200 OK / 409 Conflict / 403 Forbidden ...

Response: 200 OK / 409 Conflict / ...
```

### Получение данных об очереди

```http request
GET /api/v1/queue
body:
{
	"token": "324rb234fwf2345rf",
	"roomId": 1,
	"queueId" : 1
}
Response: 200 OK / 404 NotFound / ...
body:
{
	"id": 1,
	"name": "queue 1",
	"users": [
		{
			"name": "petr",
			"vkID": 879988134
		},
		{
			"name": "ivan",
			"vkID": 92610861
		}
	]
}

Response: 200 OK / 409 Conflict / ...
```

### Добавление пользователя в очередь

```http request
POST /api/v1/queue/join
body:
{
	"token": "324rb234fwf2345rf",
	"roomId": 1,
	"queueId" : 1
}

Response: 200 OK / 409 Conflict / ...
```

### Удаление пользователя из очереди

```http request
DELETE /api/v1/queue/leave
body:
{
	"token": "324rb234fwf2345rf",
	"roomId": 1,
	"queueId" : 1
}

Response: 200 OK / 409 Conflict / ...
```