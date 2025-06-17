# GraphQL Admin Dashboard

This document explains how to use the GraphQL-based admin dashboard for viewing booking statistics and revenue analytics.

## Overview

The admin dashboard provides comprehensive analytics including:

### 📊 Booking Analytics
- **Total bookings** from various time periods
- **Booking statistics** by week, month, year
- **Booking counts by status** (PENDING, ACTIVATE, COMPLETED, CANCELED, etc.)
- **Booking growth rates** and trends

### 💰 Revenue Analytics  
- **Total website revenue** across all services
- **Booking commission revenue**
- **Subscription revenue** from membership services
- **Revenue breakdown by service type**
- **Monthly revenue trends** and growth rates

### 📈 Dashboard Metrics
- **User statistics** (total users, dancers, choreographers)
- **Active vs expired subscriptions**
- **Average booking values**
- **Growth rate calculations**

## Getting Started

### 1. Dependencies

The project uses Spring Boot's official GraphQL starter:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-graphql</artifactId>
</dependency>
```

### 2. Run the Application

```bash
./mvnw spring-boot:run
```

### 3. Access GraphiQL Interface

Once the application is running, access the GraphiQL interface at:
```
http://localhost:8080/graphiql
```

## Available GraphQL Queries

### 1. Complete Admin Dashboard
Get all dashboard metrics in a single query:

```graphql
query GetAdminDashboard {
  getAdminDashboard {
    totalBookings
    totalUsers
    totalRevenue
    bookingRevenue
    subscriptionRevenue
    revenueGrowthRate
    activeBookings
    completedBookings
    dailyBookings {
      label
      count
      growthRate
    }
    monthlyRevenueBreakdown {
      label
      amount
      dateFrom
      dateTo
    }
  }
}
```

### 2. Booking Statistics by Date Range
Query booking data for specific time periods:

```graphql
query GetBookingStats($startDate: String!, $endDate: String!) {
  getBookingStatistics(startDate: $startDate, endDate: $endDate) {
    label
    count
    growthRate
    percentage
  }
}
```

**Variables:**
```json
{
  "startDate": "2024-01-01",
  "endDate": "2024-01-31"
}
```

### 3. Booking Statistics by Period
Get booking trends by week, month, or year:

```graphql
query GetBookingTrends($period: String!, $count: Int!) {
  getBookingStatisticsByPeriod(period: $period, count: $count) {
    label
    count
    dateFrom
    dateTo
    growthRate
  }
}
```

**Variables:**
```json
{
  "period": "MONTHLY",
  "count": 12
}
```

### 4. Booking Counts by Status
Analyze booking distribution by status:

```graphql
query GetBookingsByStatus($statuses: [String!]!) {
  getBookingCountsByStatus(statuses: $statuses) {
    label
    count
    status
    percentage
  }
}
```

**Variables:**
```json
{
  "statuses": [
    "BOOKING_PENDING",
    "BOOKING_ACTIVATE", 
    "BOOKING_COMPLETED",
    "BOOKING_CANCELED"
  ]
}
```

### 5. Revenue Analytics
Get detailed revenue breakdown:

```graphql
query GetRevenueBreakdown($startDate: String!, $endDate: String!) {
  getRevenueStatistics(startDate: $startDate, endDate: $endDate) {
    label
    amount
    currency
    transactionCount
    averageTransactionValue
  }
  
  getSubscriptionRevenue(startDate: $startDate, endDate: $endDate) {
    label
    amount
    revenueType
    transactionCount
  }
  
  getBookingRevenue(startDate: $startDate, endDate: $endDate) {
    label
    amount
    revenueType
    averageTransactionValue
  }
}
```

### 6. Total Website Revenue
Simple query for total revenue:

```graphql
query GetTotalRevenue {
  getTotalWebsiteRevenue
}
```

## Implementation Details

### GraphQL Schema
The admin dashboard types are defined in `src/main/resources/graphql/schema.graphqls`:

- `AdminDashboard` - Main dashboard type
- `BookingStatistics` - Booking analytics type  
- `RevenueStatistics` - Revenue analytics type

### Controller
`AdminDashboardGraphQLController` handles all GraphQL queries with proper admin authorization.

### Service Layer
`AdminDashboardService` and `AdminDashboardServiceImpl` contain the business logic for:
- Aggregating booking data
- Calculating revenue metrics
- Computing growth rates and trends
- Formatting time-based statistics

### Security
All dashboard queries require `ROLE_ADMIN` authorization using `@PreAuthorize("hasRole('ROLE_ADMIN')")`.

## Customization

### Adding New Metrics
1. Add new fields to response DTOs
2. Update GraphQL schema types
3. Implement calculation logic in service
4. Add new query methods in controller

### Database Queries
Replace mock data in `AdminDashboardServiceImpl` with real database queries:

```java
// Example: Replace mock booking count with real query
private Long countBookingsByStatus(String status) {
    return bookingRepository.countByStatus_StatusName(status);
}
```

### Date Range Queries
Implement repository methods with date filtering:

```java
// Example repository method
@Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingDate BETWEEN :startDate AND :endDate")
Long countBookingsByDateRange(@Param("startDate") LocalDate startDate, 
                              @Param("endDate") LocalDate endDate);
```

## Testing

### Unit Tests
Test the service layer with mock data:

```java
@Test
void shouldReturnAdminDashboard() {
    AdminDashboardResponse response = adminDashboardService.getAdminDashboard();
    assertThat(response.totalBookings()).isGreaterThan(0);
    assertThat(response.totalRevenue()).isPositive();
}
```

### Integration Tests
Test GraphQL queries with `@GraphQLTest`:

```java
@GraphQlTest(AdminDashboardGraphQLController.class)
class AdminDashboardGraphQLTest {
    
    @Test
    void shouldReturnDashboardData() {
        String query = """
            query {
                getAdminDashboard {
                    totalBookings
                    totalRevenue
                }
            }
            """;
        
        graphQlTester.document(query)
            .execute()
            .path("getAdminDashboard.totalBookings")
            .hasValue();
    }
}
```

## Next Steps

1. **Replace Mock Data**: Implement real database queries in the service layer
2. **Add Caching**: Use `@Cacheable` for expensive aggregation queries  
3. **Add Pagination**: Implement pagination for large result sets
4. **Real-time Updates**: Consider WebSocket subscriptions for live dashboard updates
5. **Export Features**: Add queries to export data in CSV/Excel format

## Sample Queries File

See `src/main/resources/graphql/admin-dashboard-queries.graphql` for comprehensive query examples.

This GraphQL approach provides a flexible, type-safe way to build powerful admin dashboards with precise data fetching and excellent developer experience. 