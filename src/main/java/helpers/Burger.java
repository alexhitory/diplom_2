package helpers;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    @Data
    public class Burger {
        private List<String> ingredients;

        public Burger(List<String> ingredients) {
            this.ingredients = ingredients;
        }

        public Burger() {
            ingredients = new ArrayList<>();
        }

        public static Burger burgerWithCorrectIngredients() {
            return new Burger(new ArrayList<>(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa6d")));
        }

        public static Burger burgerWithIncorrectIngredients() {
            return new Burger(new ArrayList<>(Arrays.asList("61c0c5a71d1f820", "61c0c5a71d1f820", "61c0c5a71d1f820")));
        }
    }

