package ApiTestsPackage;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Epic("GET запросы")
public class GetOperatorTests {
    @Test
    @DisplayName("Список всех операций пользователя.")
    @Description("GET запрос с параметром operators. Возвращает список доступных операторов")
    @Tag("Положительные")
    @Tag("GET")
    public void OperatorsAll(){

    }
    @ParameterizedTest
    @ValueSource(strings = {"+","-","*","/","="})
    @DisplayName("Список операций для определенного оператора")
    @Description("GET запрос с параметром operators и одним из операторов(+, -, *, /, =)")
    @Tag("Положительные")
    @Tag("GET")
    public void OperatorsSome(){

    }
}
