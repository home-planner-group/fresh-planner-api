package utility;

import com.freshplanner.api.model.authentication.RegistrationModel;
import com.freshplanner.api.model.product.ProductModel;
import com.freshplanner.api.model.recipe.RecipeModel;
import com.freshplanner.api.service.enums.Unit;

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

    public static class Recipe {
        public static RecipeModel recipeModelV1(Integer id) {
            return new RecipeModel(
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

        public static RecipeModel recipeModelV2(Integer id) {
            return new RecipeModel(
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

        public static RecipeModel.Item recipeItemV1(Integer productId) {
            return new RecipeModel.Item(
                    productId,
                    "Placeholder",
                    1f,
                    null,
                    "NoDescription"
            );
        }

        public static RecipeModel.Item recipeItemV2(Integer productId) {
            return new RecipeModel.Item(
                    productId,
                    "Placeholder",
                    2f,
                    null,
                    "NoDescriptionEdited"
            );
        }
    }
}
