<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
</head>
<body>
<div class="main-content">
<h1>
    ⬛ Project: hw17-grpc
</h1>
    <div class="task">
        <b>Status:</b> ✅ Resolved
        <br><b>Name:</b> «gRPC клиент-серверное приложение»
        <br><b>Goal:</b> Научиться разрабатывать сетевые приложения с gRPC
        <h2>Steps:</h2>
        <details open>
        <summary><b>Server Part</b></summary>
        <ul>
            <li>Сервер по запросу клиента генерирует последовательность чисел</li>
            <li>Запрос от клиента содержит начальное значение (firstValue) и конечное (lastValue)</li>
            <li>Раз в две секунды сервер генерирует новое значение и стримит его клиенту:</li>
            > firstValue + 1<br>
            > firstValue + 2<br>
            > ...<br>
            > lastValue<br>
        </ul>
    </details>
    <details open>
        <summary><b>Client Part</b></summary>
        <ol>
            <li>Клиент после своего старта запускает генерацию последовательности чисел от 0 до 50</li>
            <li>Одновременно с этим отправляет запрос на сервер, чтобы получить последовательность чисел от 0 до 30</li>
            <li>Из полученных чисел (т.е. из числа формируемого внутри цикла и от числа формируемого на сервере) считается
            некоторое конечное число по формуле:</li>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i>currentValue = [currentValue] + [ПОСЛЕДНЕЕ число от сервера] + 1;</i><br>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Начальное значение: currentValue = 0<br>
            <li>При этом число, получаемое от сервера, должно учитываться только один раз.<br>
            Обратите внимание, что сервер может вернуть несколько числен, надо взять именно ПОСЛЕДНЕЕ.</li>
        </ol>
    </details>
    <details open>
        <summary><b>Result</b></summary>
        <p>Должно получиться примерно так:</p>
        <ol>
            <li>
                currentValue: 1<br>
                Число от сервера: 2<br>
                В консоль выводим: 4 (curr 1 + server 2 = 3 и увеличиваем result на 1)<br>
            </li>
            <li>
                <i>Следующая итерация цикла..</i><br>
                currentValue: 5 (к currentValue с предыдущего шага прибавляем 1)<br>
                <i>Т.к. значение сервера мы получить не успели, ничего не добавляем, </i>Число от сервера<i> = 0</i>
            </li>
            <li>
                <i>Следующая итерация..</i><br>
                Получаем число от сервера: 3<br>
                CurrentValue: 9 (3 + prev: 5 + 1)
            </li>
            <li>
                currentValue: 10 (prev currValue = 9 + 1)<br>
                <i>От сервера получить ничего не успели, </i>Число от сервера<i> = 0</i>
            </li>
            <li><i>etc...</i></li>
        </ol>
    </details>
    </div>
</div>
</body>
</html>