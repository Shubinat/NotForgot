# Not Forgot!

Not Forgot! - это приложение которое поможет вам не забыть ваши задачи и грамотно распределять свой график.

## Интерфейс
<div style="display:flex;">
  <img src="https://github.com/Shubinat/NotForgotImages/blob/main/auth.jpg" width="150"/>
  <img src="https://github.com/Shubinat/NotForgotImages/blob/main/reg.jpg" width="150"/>
  <img src="https://github.com/Shubinat/NotForgotImages/blob/main/list.jpg" width="150"/>
  <img src="https://github.com/Shubinat/NotForgotImages/blob/main/details.jpg" width="150"/>
</div>
<div style="display:flex;">
  <img src="https://github.com/Shubinat/NotForgotImages/blob/main/edit_note.jpg" width="150"/>
  <img src="https://github.com/Shubinat/NotForgotImages/blob/main/category_creation.jpg" width="150"/>
  <img src="https://github.com/Shubinat/NotForgotImages/blob/main/add_note.jpg" width="150"/>
  
</div>

## Запуск проекта

1. Кнонируйте <a href="https://github.com/Shubinat/NotForgotAPI">резпозиторий с API</a> и разверните его по адресу ```http://192.168.1.94:80/notforgot```

2. Кнопируйте репозиторий с помощью Android Studio и нажмите кнопку ''Run''

## Архитектура

Приложение основано на MVVM архитектуре с использованием паттерна Repository

<img src="https://github.com/Shubinat/NotForgotImages/blob/main/achritecture.png" width="620"/>

## Технологический стэк

* Язык программирования Kotlin
* Minimum SDK 26
* MVVM Architecture
* ViewBinding
* Architecture Components (Lifecycle, LiveData, ViewModel, Room Persistence)
* <a href="https://developer.android.com/guide/navigation/navigation-getting-started">Fragment Navigation</a>
* <a href="https://github.com/Shubinat/NotForgotAPI">API, написанное мной на C#</a>
* <a href="https://square.github.io/retrofit/">Retrofit для взаимодействия с API</a>
* <a href="https://github.com/google/gson">Gson для серелизации Json</a>
* <a href="https://developer.android.com/reference/androidx/work/WorkManager">WorkManager, для синхронизации данных при отсутствии интернета</a>

## Заметки

Приложение делалось по <a href="https://www.figma.com/file/3rqvRtPyR9Thr39x8SIVRi/NotForgot?t=btDIvOoPvpLInXyS-0">макету от моего преподавателя</a>, но после было принято решение сделать его в фиолетовой палитре. Также в техническом задании не было информации о хранении данных, поэтому я реализовал загрузку данных из API и хранение в базе данных при отсутствии интернета. 

## Контакты

По всем вопросам пишите в Telegram: <a href="http://t.me/shubinat">@Shubinat</a>
