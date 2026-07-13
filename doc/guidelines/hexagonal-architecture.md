### What is Hexagonal Architecture

The hexagonal architectural pattern aims to create loosely coupled application components that can be easily connected
to their software environment through ports and adapters.
Here is a brief overview:

**Key Concepts:**

- **Core Domain:** At the center of the architecture is the core domain, which contains the business logic and rules. This core is independent of external systems and frameworks.
- **Ports:** These are interfaces that define how the core domain interacts with the outside world. Ports can be thought of as entry points for different types of interactions, such as user inputs, database operations, or external service calls.
- **Adapters:** Adapters implement the ports and handle the communication between the core domain and external systems. There are two main types of adapters:
    - **Input Adapters:** These handle incoming requests, such as user interfaces or API controllers.
    - **Output Adapters:** These handle outgoing requests, such as database repositories or external service clients.

**Benefits:**

- **Separation of Concerns:** By isolating the business logic from external systems, it becomes easier to maintain and test the core domain.
- **Flexibility:** Adapters can be swapped out or modified without affecting the core domain, making the system more adaptable to changes.
- **Testability:** The core domain can be tested independently of external systems, leading to more reliable and robust tests.

**When to Use:**

- **Complex Interactions:** If the application needs to interact with multiple external systems (e.g., databases, third-party services), hexagonal architecture can provide better flexibility.
- **Testability:** If required extensive testing of the business logic independently of external systems, hexagonal architecture is beneficial.
- **Flexibility and Adaptability**: When expecting changes in external systems or need to support multiple types of interfaces (e.g., REST, messaging), hexagonal architecture offers more adaptability.

### Project Structure

The project structure is well-organized and thoughtfully separates
concerns according to hexagonal architecture principles.

```
├───src
│   ├───main
│   │   ├───java
│   │   │   └───com
│   │   │       └───github
│   │   │           └───ehayik
│   │   │               └───coindesk
│   │   │                   ├───common
│   │   │                   ├───infrastructure
│   │   │                   │   ├───adapter
│   │   │                   │   │   ├───in
│   │   │                   │   │   └───out
│   │   │                   │   │   └───shared
│   │   │                   ├───application
│   │   │                   │       ├───domain
│   │   │                   │       │   ├───model
│   │   │                   │       │   └───service
│   │   │                   │       ├───ports
│   │   │                   │       │   ├───in
│   │   │                   │       │   └───out
│   │   │                   │       └───usecase
```

**Key Strengths:**

1. **Feature-First Organization**:
- Structuring around domain-bounded context (`coindesk`) makes it easier to understand the system's capabilities
    - **coindesk:** Responsible for cryto currencies price updates.
- Enables teammates to work on separate features with minimal conflicts
- Makes it clear which code belongs to which business capability

2. **Consistent Hexagonal Pattern Within Features**:
- Each feature follows the same internal organization (domain, ports, use case)
- Maintains consistent boundaries between layers across features
- Makes the codebase easier to navigate once you understand the pattern

3. **Clean Separation of Concerns**:
- Clear distinction between domain models and services
- Ports are separated by direction (in/out)
- Infrastructure adapters are properly isolated

4. **Shared Infrastructure**:
- Common infrastructure adapters that can be used across features
- Avoids duplication of adapter implementations

**Specific Architectural Elements:**

- **Domain Core**: Properly isolated in `domain/model` and `domain/service` folders
- **Ports**: Clean separation between `in` and `out` ports
- **Use Cases**: Dedicated space for application services that implement business workflows
- **Adapters**: Centralized in `infrastructure/adapter`, with clear direction separation

### Naming Conventions

- **Input Port**: Use the prefix `ForRequesting` followed by the action and entity
    - Examples: `ForRequestingVehicleCreation`, `ForRequestingBankAccounts`
    - This pattern emphasizes that these ports represent requests coming into the application
- **Output Port**: Use the prefix `For` followed by the operation and entity
    - Examples: `ForSearchingCustomers`, `ForSavingPayment`
    - This pattern focuses on the operation being performed by the application toward external systems
- **Use Case**: Follows the common pattern of "<Action><Entity>UseCase". e.g `LoadPaymentsUseCase`
- **Persistence Adapter**: Follows the common pattern of "<Entity>PersistenceAdapter" e.g `CustomerPersistenceAdapter`

> **NOTE**:
> When creating new ports,
> always use the appropriate prefix based on whether it is an input or output port to maintain architectural clarity.

### Use Case Orchestration

Use case orchestration refers to how the application layer manages the flow of operations for a specific
business use case, acting as a **mediator** between input adapters (like REST controllers) and the core domain logic.

We should use _Pipelinr_ as a **mediator**.
It aligns well with the Command Pattern and CQRS principles, providing
a clean separation between input adapters and use cases.

- Commands act as input ports, creating a clear contract
- Handlers (use cases) implement the business logic without knowing about the caller
- Input adapters (REST controllers, event listeners, schedulers, etc.) should be thin wrappers that:
    - Create command objects from requests.
    - Send them through the pipeline
    - Transform responses for clients
- Use Case:
    - Translates between command and domain models
    - Manage data communicating with ports and domain services
    - Perform business logic and rules.
- Domain Service:
    - Should be the primary location for defining **transaction boundaries**.
    - It can perform business logic and rules reusable across multiple use cases if needed it.
- Domain entities contain related data and behavior
    - It may be immutable

```java
import java.util.Optional;

// Input Port (Command)
@Builder
public record ForRequestingMoneyTransfer(
        String sourceAccountId,
        String targetAccountId,
        BigDecimal amount) implements Command<Voidy> {
}

// Use Case (Handler)
@UseCase
@Transactional
@RequiredArgsConstructor
public class TransferMoneyUseCase implements Command.Handler<ForRequestingMoneyTransfer, Voidy> {

    private final ForLoadingAccounts forLoadingAccounts;
    private final ForRecordingTransaction forRecordingTransaction;

    @Override
    public Voidy handle(ForRequestingMoneyTransfer input) {
        // 1. Retrieve accounts
        // 2. Validate business rules
        // 3. Execute transfer
        // 4. Record transaction
        return new Voidy();
    }
}

// Input Adapter (REST Controller)
@RestController
@RequiredArgsConstructor
class TransferController {

    private final Pipeline pipeline;

    @PostMapping("/transfers")
    @ResponseStatus(ACCEPTED)
    public void transferMoney(@RequestBody TransferRequest request) {
        ForRequestingMoneyTransfer.builder()
                .sourceAccountId(request.getSourceAccountId())
                .targetAccountId(request.getTargetAccountId())
                .amount(request.getAmount())
                .build()
                .execute(pipeline);
    }
}

// Output Adapter (Persistence Adapter)
@Component
@RequiredArgsConstructor
class AccountPersistenceAdapter implements ForLoadingAccounts {

    private final JpaAccountRepository accountRepository;

    @Override
    public Optional<Account> findAccount(String accountId) {
        return Optional.empty();
    }
}
```