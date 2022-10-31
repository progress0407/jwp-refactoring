package kitchenpos.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;

public class TableGroupRequest {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(LocalDateTime createdDate, List<OrderTableRequest> orderTableRequests) {
        this(null, createdDate, orderTableRequests);
    }

    public TableGroupRequest(Long id, LocalDateTime createdDate, List<OrderTableRequest> orderTableRequests) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTableRequests;
    }

    public TableGroup toDomain() {

        final List<OrderTable> orderTables = extractOrderTables();

        return new TableGroup(id, createdDate, orderTables);
    }

    private List<OrderTable> extractOrderTables() {
        return this.orderTables.stream()
                .map(OrderTableRequest::toDomain)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    @Override
    public String toString() {
        return "TableGroupRequest{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", orderTables=" + orderTables +
                '}';
    }
}
