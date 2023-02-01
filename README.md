# Explore-with-me 
Explore with me (англ. «исследуй со мной») - это афиша, с помощью которой можно делиться
предстоящими событиями и находить компанию. Это многомодульное приложение с двумя микросервисами.

**Приложение делится на два сервиса:**
1. Основной сервис - будет содержать всё необходимое для работы продукта. Будет делиться на три части:
* публичная будет доступна без регистрации любому пользователю сети;
* закрытая будет доступна только авторизованным пользователям;
* административная — для администраторов сервиса.
2. Сервис статистики - будет хранить количество просмотров и позволит делать различные выборки 
для анализа работы приложения. Функционал сервиса статистики будет содержать:
* запись информации о том, что был обработан запрос к эндпоинту API;
* предоставление статистики за выбранные даты по выбранному эндпоинту.

На данном этапе реализован сервис статистики, хранение статистической информации в базе данных.