Питала: чому ValidationHelper червоним у рядку 27, 28 у файлі EquipmentItem.

АІ відповів, що це відбувається тому, що у файлі ⁠ValidationHelper.java⁠ методи мають модифікатор package-private (тобто не мають слова ⁠public⁠ перед ⁠static String validateEnum⁠). Оскільки ⁠ValidationHelper⁠ лежить у пакеті ⁠util⁠, а ⁠EquipmentItem⁠ у пакеті ⁠model⁠, ⁠model⁠ не бачить не-публічні методи з ⁠util⁠ і що потрібно зробити методи у ValidationHelper.java публічними

Але в завданні ValidationHelper має бути приватним, тому я йому це написала.

АІ відповів: «Зверніть увагу: класи з ⁠model⁠ лежать в іншому пакеті, тому ⁠ValidationHelper⁠ і ⁠FormatHelper⁠ їм не видно. Єдиний вхід — ⁠<Домен>Utils⁠». Це означає, що моделі взагалі НЕ повинні викликати ⁠ValidationHelper⁠ напряму. Замість цього сутності та інші класи повинні використовувати ⁠RentalUtils⁠, а вже ⁠RentalUtils⁠ усередині свого пакета ⁠util⁠ делегує перевірки та форматування до ⁠ValidationHelper⁠ і ⁠FormatHelper⁠. 

І це вже було правильно.