<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
</head>
<body>
<div class="main-content">
<h1>
    ⬛ Project: hw03-reflection
</h1>
    <div class="task">
        <b>Status:</b> ✅ Resolved
        <br><b>Name:</b> «‎Свой тестовый фреймворк»
        <br><b>Goal:</b> Написать свой тестовый фреймворк
        <h2>Steps:</h2>
        <ol>
            <li>
                Создать три аннотации - @Test, @Before, @After.
            </li>
            <li>
                Создать класс-тест, в котором будут методы, отмеченные аннотациями.
            </li>
            <li>
                Создать "запускалку теста". На вход она должна получать имя класса с тестами, в котором следует найти и 
                запустить методы, отмеченные аннотациями и пункта 1.
            </li>
            <li>
                Алгоритм запуска должен быть следующий: сперва метод(ы) Before, затем текущий метод Test, после метод(ы)
                After; для каждой такой "тройки" надо создать <u>свой</u> объект класса-теста.
            </li>
            <li>
                Исключение в одном тесте не должно прерывать весь процесс тестирования.
            </li>
            <li>
                На основании возникших во время тестирования исключений вывести статистику выполнения тестов (сколько
                прошло успешно, сколько упало, сколько было всего)
            </li>
            <li>
                "Запускалка теста" не должна иметь состояние, но при этом весь функционал должен быть разбит на 
                приватные методы. Надо придумать, как передавать информацию между методами.
            </li>
        </ol>
    </div>
</div>
</body>
</html>