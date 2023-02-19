# Explore-with-me

Explore with me (англ. «исследуй со мной») - это афиша, с помощью которой можно делиться
предстоящими событиями и находить компанию. Это многомодульное приложение с двумя микросервисами.

**Приложение делится на два сервиса:**

1. Основной сервис - содержит всё необходимое для работы продукта. Делится на три части:

* публичная - доступна без регистрации любому пользователю сети;
* закрытая - доступна только авторизованным пользователям;
* административная — для администраторов сервиса.
  

  Публичный API предоставляет возможности поиска и фильтрации событий.

* сортировка списка событий организована либо по количеству просмотров, 
  которое будет запрашиваться в сервисе статистики, либо по датам событий;
* при просмотре списка событий возвращается только краткая информация о мероприятиях;
* каждый публичный запрос для получения списка событий или полной информации о мероприятии  
  фиксируется сервисом статистики.


  Закрытая часть API реализовывает возможности зарегистрированных пользователей продукта.

* авторизованные пользователи имеют возможность добавлять в приложение новые мероприятия, 
  редактировать их и просматривать после добавления;
* возможность подачи заявок на участие в интересующих мероприятиях;
* создатель мероприятия имеет возможность подтверждать заявки, которые отправили другие пользователи сервиса.


  Административная часть API предоставляет возможности настройки и поддержки работы сервиса.

* администратор имеет возможность добавлять, изменять и удалять категорий для событий; 
* администратор может добавлять, удалять и закреплять на главной странице подборки мероприятий;
* модерацию событий, размещённых пользователями, — публикация или отклонение;
* управление пользователями — добавление, активация, просмотр и удаление.

2. Сервис статистики - хранит количество просмотров и позволяет делать различные выборки
   для анализа работы приложения. 

  Функционал сервиса статистики содержит:

* запись информации о том, что был обработан запрос к эндпоинтам API (GET /events, GET /events/{id});
* предоставление статистики за выбранные даты по выбранному эндпоинту.


  **Дополнительная функция - комментарии к событиям.**

Делится на три части:

* публичная - доступна без регистрации любому пользователю сети;
* закрытая - доступна только авторизованным пользователям;
* административная — для администраторов сервиса.


  Публичная - возможность просматривать опубликованные комментарии:
* сортировка комментариев организована по дате создания либо по умолчанию по id;


  Закрытая - реализовывает возможности зарегистрированных пользователей продукта:
* добавление комментария к опубликованному событию;
* редактирование или удаление комментария, если он  еще не прошел модерацию;
* получение комментария по id;


  Административная - модерация содержания комментариев:
* модерация комментариев, размещённых пользователями, — публикация или отклонение;
 

  **Жизненный цикл события включает несколько этапов:**

  * Создание. 
  * Ожидание публикации. В статус ожидания публикации событие переходит сразу после создания. 
  * Публикация. В это состояние событие переводит администратор. 
  * Отмена публикации. В это состояние событие переходит в двух случаях. Первый — если администратор решил, 
    что его нельзя публиковать. Второй — когда инициатор события решил отменить его на этапе ожидания публикации.

  Данные основного сервиса и сервиса статистики сохраняют и выгружают данные из разных баз данных.
  Взаимодействие сервисов в момент сохранения информации о том, что был обработан запрос к эндпоинтам API
  происходит при помощи клиента статистики (stat-client).
  
  Диаграмма БД основного сервиса
  ![](https://github.com/DaryaSerova/java-explore-with-me/blob/feature_comments/db%20-%20explore%20with%20me%20-%20main%20service.png)

  Диаграмма БД сервиса статистики
  ![](https://github.com/DaryaSerova/java-explore-with-me/blob/feature_comments/db%20-%20explore%20with%20me%20-%20statistic%20service.png)