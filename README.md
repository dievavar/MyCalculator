Веб-приложение на Spring Boot: Калькуляторы и управление пользователями

Это веб-приложение, разработанное на Spring Boot, предоставляет два калькулятора: конвертер валют и калькулятор закона Ома. Приложение также включает систему управления пользователями с ролями администратора и обычного пользователя. Все операции сохраняются в базе данных PostgreSQL.


Основные функции

Конвертер валют:

Курсы обмена валют сохраняются в базе данных администратором.
Пользователи могут конвертировать валюты на основе актуальных курсов.

Калькулятор закона Ома (V = IR):

Если известны любые две переменные (напряжение, ток или сопротивление), третья вычисляется автоматически.
Каждая операция сохраняется в базе данных.
Управление пользователями

Регистрация и вход в систему:
Пользователи могут регистрироваться и входить в систему.

Роли:

Администратор:

Управление пользователями (добавление, удаление, редактирование).

Просмотр всей истории операций.

Внесение и обновление курсов обмена валют.

Пользователь:

Выполнение вычислений с использованием калькуляторов.

Просмотр только своей истории операций.


Технологии

Spring Boot: основа приложения, включает модули Web, JPA, Security и Thymeleaf.

Spring Security: обеспечивает аутентификацию и ролевую авторизацию.

PostgreSQL: база данных для хранения пользователей, истории операций и курсов валют.

КОД:

-- Создание таблицы для операций
CREATE TABLE ohm_law_calculation (
    id SERIAL PRIMARY KEY,          -- Уникальный идентификатор операции
    voltage DOUBLE PRECISION,       -- Напряжение (V)
    current DOUBLE PRECISION,       -- Ток (I)
    resistance DOUBLE PRECISION,    -- Сопротивление (R)
    calculated_value VARCHAR(50),   -- Какое значение было вычислено (V, I или R)
    user_id BIGINT,                 -- Ссылка на пользователя
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) -- Внешний ключ
);
select * from ohm_law_calculation;



select * from users;



CREATE TABLE exchange_rate (
    id SERIAL PRIMARY KEY, -- Уникальный идентификатор
    from_currency VARCHAR(3) NOT NULL, -- Валюта, из которой конвертируем (например, USD)
    to_currency VARCHAR(3) NOT NULL,   -- Валюта, в которую конвертируем (например, EUR)
    rate DOUBLE PRECISION NOT NULL,    -- Курс обмена
    UNIQUE (from_currency, to_currency) -- Уникальная пара валют
);


CREATE TABLE currency_conversion (
    id SERIAL PRIMARY KEY, -- Уникальный идентификатор
    from_currency VARCHAR(10) NOT NULL, -- Валюта, из которой конвертируем (например, USD)
    to_currency VARCHAR(10) NOT NULL,   -- Валюта, в которую конвертируем (например, EUR)
    amount DOUBLE PRECISION NOT NULL,  -- Сумма для конвертации
    result DOUBLE PRECISION NOT NULL,  -- Результат конвертации
    user_id BIGINT NOT NULL,           -- Пользователь, выполнивший операцию
    exchange_rate_id BIGINT NOT NULL,  -- Курс обмена, использованный для операции

    -- Внешний ключ на таблицу пользователей (my_user)
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),

    -- Внешний ключ на таблицу курсов обмена (exchange_rate)
    CONSTRAINT fk_exchange_rate FOREIGN KEY (exchange_rate_id) REFERENCES exchange_rate(id)
);


Thymeleaf: шаблонизатор для создания динамических веб-страниц.

Bootstrap: используется для стилизации фронтенда.

HTML/CSS: для создания пользовательского интерфейса.


<img width="1680" alt="Снимок экрана 2025-03-23 в 17 15 28" src="https://github.com/user-attachments/assets/b9efb08b-0a09-4b11-b4c6-7144ffa5ffa0" />

<img width="1680" alt="Снимок экрана 2025-03-23 в 17 16 09" src="https://github.com/user-attachments/assets/ea30e83c-798f-4a88-9c8f-b04f33c86e72" />

<img width="1680" alt="Снимок экрана 2025-03-23 в 17 16 47" src="https://github.com/user-attachments/assets/43d4594f-b876-4a11-8a9a-8b8cc579d937" />

<img width="1680" alt="Снимок экрана 2025-03-23 в 17 17 10" src="https://github.com/user-attachments/assets/734dc7f2-17be-4723-beb8-043002bb413a" />

<img width="1680" alt="Снимок экрана 2025-03-23 в 17 17 37" src="https://github.com/user-attachments/assets/90fe608a-bafb-4fa2-bb0f-545cdcba389a" />

<img width="1680" alt="Снимок экрана 2025-03-23 в 17 18 23" src="https://github.com/user-attachments/assets/8f593be1-bd8d-4036-aa88-50e62808928d" />

<img width="1671" alt="Снимок экрана 2025-03-23 в 17 19 13" src="https://github.com/user-attachments/assets/d1f7f3d6-6504-449d-9d96-04d2b171ccda" />

