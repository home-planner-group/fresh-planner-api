package utility;

import com.freshplanner.api.database.enums.Unit;
import com.freshplanner.api.model.authentication.RegistrationModel;
import com.freshplanner.api.model.product.ProductModel;

/**
 * Data generator for tests.
 */
public class DataFactory {
    public static class User {

        // === USER ========================================================================================================

        public static RegistrationModel registration() {
            return new RegistrationModel(
                    "TestUser1",
                    "Test@email.com",
                    "TestPassword");
        }
    }

    public static class Product {
        public static ProductModel productModelV1(Integer id) {
            return new ProductModel(
                    id,
                    "TestProduct",
                    "TestCategory",
                    Unit.GRAM,
                    500f,
                    100f,
                    10f,
                    20f,
                    30f);
        }

        public static ProductModel productModelV2(Integer id) {
            ProductModel modelV1 = productModelV1(id);
            return new ProductModel(
                    id,
                    modelV1.getName(),
                    modelV1.getCategory(),
                    Unit.ML,
                    modelV1.getPackageSize() * 2,
                    modelV1.getKcal() * 2,
                    modelV1.getCarbohydrates() * 2,
                    modelV1.getProtein() * 2,
                    modelV1.getFat() * 2);
        }
    }
}
