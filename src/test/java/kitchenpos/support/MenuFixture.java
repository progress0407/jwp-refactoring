package kitchenpos.support;

import static org.assertj.core.util.Lists.emptyList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public abstract class MenuFixture {

    public static Menu createMenu(final int price) {

        return new Menu("치킨은 살 안쪄요, 살은 내가 쪄요", BigDecimal.valueOf(price), 1L, emptyList());
    }

    public static Menu createMenu(final int price, final long menuGroupId) {

        return new Menu("치킨은 살 안쪄요, 살은 내가 쪄요", BigDecimal.valueOf(price), menuGroupId, emptyList());
    }

    public static Menu createMenu(final int price, final List<MenuProduct> menuProducts) {

        return new Menu("치킨은 살 안쪄요, 살은 내가 쪄요", BigDecimal.valueOf(price), 1L, menuProducts);
    }

    public static Menu createMenuWithProduct(final Long productId, final int price) {

        return new Menu("치킨은 살 안쪄요, 살은 내가 쪄요", BigDecimal.valueOf(price), 1L, List.of(new MenuProduct(productId, 1L)));
    }

    public static WrapMenu createMenuRequest(final String name,
                                             final int price,
                                             final Long menuGroupId,
                                             final List<MenuProduct> menuProducts) {

        return new WrapMenu(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public static class WrapMenu extends Menu {

        /**
         * need jackson binding default constructor
         */
        public WrapMenu() {
        }

        public WrapMenu(final String name,
                        final BigDecimal price,
                        final Long menuGroupId,
                        final List<MenuProduct> menuProducts) {

            super(name, price, menuGroupId, menuProducts);
        }

        public Long id() {
            return super.getId();
        }

        public String name() {
            return super.getName();
        }

        public BigDecimal price() {
            return super.getPrice();
        }

        public double doublePrice() {
            return super.getPrice().doubleValue();
        }

        public int intPrice() {
            return super.getPrice().intValue();
        }
    }
}
