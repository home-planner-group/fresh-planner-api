package utility;

import com.freshplanner.api.controller.model.authentication.RegistrationModel;
import com.freshplanner.api.enums.Unit;

import java.util.ArrayList;

/**
 * Data generator for tests.
 */
public class DataFactory {
    public static class User {
        public static RegistrationModel registration() {
            return new RegistrationModel(
                    "TestUser1",
                    "Test@email.com",
                    "TestPassword");
        }
    }

    public static class Product {
        public static com.freshplanner.api.controller.model.Product productModelV1(Integer id) {
            return new com.freshplanner.api.controller.model.Product(
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

        public static com.freshplanner.api.controller.model.Product productModelV2(Integer id) {
            com.freshplanner.api.controller.model.Product modelV1 = productModelV1(id);
            return new com.freshplanner.api.controller.model.Product(
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

    public static class Recipe {
        public static com.freshplanner.api.controller.model.Recipe recipeModelV1(Integer id) {
            return new com.freshplanner.api.controller.model.Recipe(
                    id,
                    "TestRecipe",
                    "TestCategory",
                    15,
                    "TestDescription",
                    1f,
                    2f,
                    3f,
                    4f,
                    new ArrayList<>()
            );
        }

        public static com.freshplanner.api.controller.model.Recipe recipeModelV2(Integer id) {
            return new com.freshplanner.api.controller.model.Recipe(
                    id,
                    "TestRecipeEdited",
                    "TestCategoryEdited",
                    30,
                    "TestDescriptionEdited",
                    1f,
                    2f,
                    3f,
                    4f,
                    new ArrayList<>()
            );
        }

        public static com.freshplanner.api.controller.model.Recipe.Item recipeItemV1(Integer productId) {
            return new com.freshplanner.api.controller.model.Recipe.Item(
                    productId,
                    "Placeholder",
                    1f,
                    null,
                    "NoDescription"
            );
        }

        public static com.freshplanner.api.controller.model.Recipe.Item recipeItemV2(Integer productId) {
            return new com.freshplanner.api.controller.model.Recipe.Item(
                    productId,
                    "Placeholder",
                    2f,
                    null,
                    "NoDescriptionEdited"
            );
        }
    }
}
