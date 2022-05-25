<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
</head>
<body>
<div class="main-content">
<h1>
    ⬛ Project: hw05-aop
</h1>
    <div class="task">
        <b>Status:</b> ✅ Resolved
        <br><b>Name:</b> «Автоматическое логирование»
        <br><b>Goal:</b> Понять как реализуется AOP, какие для этого есть технические средства
        <h2>Steps:</h2>
        <ol>
            <li>
                Разработайте такой функционал: метод класса можно пометить самодельной аннотацией @Log, например так:
                <br>class TestLogging {
                <br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp@Log
                <br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp public void calculation(int param) {};
                <br>}
            </li>
            <li>
                При вызове этого метода "автоматически" в консоль должны логироваться значения параметров. Обратите 
                внимание: явного вызова логирования быть не должно. В консоли должно быть: executed method: calculation,
                param: 6
            </li>
        </ol>
    </div>
</div>
</body>
</html>