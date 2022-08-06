<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
</head>
<body>
<div class="main-content">
<h1>
    ⬛ Project: hw13-di
</h1>
    <div class="task">
        <b>Status:</b> ✅ Resolved
        <br><b>Name:</b> «Собственный IoC контейнер»
        <br><b>Goal:</b> В процессе создания своего контекста понять как работает основная часть Spring framework.
        <h2>Steps:</h2>
        <ol>
            <li>
                Скачать заготовку приложения тренажера таблицы умножения из репозитория с примерами
            </li>
            <li>
                В классе AppComponentsContainerImpl реализовать обработку, полученной в конструкторе конфигурации, основываясь на разметке аннотациями из пакета appcontainer. Так же необходимо реализовать методы getAppComponent</li>
            <li>
                В итоге должно получиться работающее приложение. Менять можно только класс AppComponentsContainerImpl</li>
            <li>
                (Дополнительно) Разделить AppConfig на несколько классов и распределить по ним создание компонентов. В AppComponentsContainerImpl добавить конструктор, который обрабатывает несколько классов-конфигураций
            </li>
            <li>
                (Дополнительно) В AppComponentsContainerImpl добавить конструктор, который принимает на вход имя пакета, и обрабатывает все имеющиеся там классы-конфигурации (см. зависимости в pom.xml)
            </li>
        </ol>
    </div>
</div>
</body>
</html>