package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.presentation.dto.request.TableGroupRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.TableRepository;
import kitchenpos.order.repository.TableGroupRepository;
import kitchenpos.order.specification.TableGroupSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupSpecification tableGroupSpecification;

    public TableGroupService(OrderRepository orderRepository,
                             TableRepository tableRepository,
                             TableGroupRepository tableGroupRepository,
                             TableGroupSpecification tableGroupSpecification) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupSpecification = tableGroupSpecification;
    }

    @Transactional
    public TableGroup create(TableGroupRequest request) {

        List<Long> requestTableIds = request.tableIds();

        List<OrderTable> savedTables = tableRepository.findAllByIdIn(requestTableIds);

        tableGroupSpecification.validateCreate(request, savedTables);

        TableGroup tableGroup = new TableGroup();
        tableGroup.mapTables(savedTables);

        tableGroup.initCurrentDateTime();
        tableGroup.changeStatusNotEmpty();

        return tableGroupRepository.save(tableGroup);
    }


    @Transactional
    public void ungroup(final Long tableGroupId) {

        List<OrderTable> orderTables = tableRepository.findAllWithGroupByTableGroupId(tableGroupId);

        tableGroupSpecification.validateUngroup(orderTables);

        for (OrderTable orderTable : orderTables) {
            final TableGroup tableGroup = orderTable.getTableGroup();
            orderTable.changeStatusNotEmpty();
            orderTable.ungroup();
            tableGroupRepository.delete(tableGroup);
        }

        tableRepository.saveAll(orderTables);
    }
}
