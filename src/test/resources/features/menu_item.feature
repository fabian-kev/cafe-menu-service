Feature: Menu Item Management
  As a tenant
  I want to manage menu items
  So that I can maintain my menu offerings

  Scenario: Creating a new menu item with valid data
    Given a tenant with ID "550e8400-e29b-41d4-a716-446655440000"
    When I create a menu item with name "Burger" and price 9.99
    Then the menu item should be created successfully
    And the response should contain the name "Burger" and price 9.99

  Scenario: Creating a menu item with invalid data
    Given a tenant with ID "550e8400-e29b-41d4-a716-446655440000"
    When I create a menu item with name "" and price -1
    Then the creation should fail with validation errors
    And the response should contain errors including "Name is required" and "Price must be non-negative"

#  Scenario: Updating an existing menu item
#    Given a tenant with ID "550e8400-e29b-41d4-a716-446655440000"
#    Given an existing menu item with ID "123e4567-e89b-12d3-a456-426614174000"
#    When I update the menu item with name "Updated Burger" and price 10.99
#    Then the menu item should be updated successfully
#    And the response should contain the name "Updated Burger" and price 10.99
#
#  Scenario: Retrieving a menu item by ID
#    Given a tenant with ID "550e8400-e29b-41d4-a716-446655440000"
#    Given an existing menu item with ID "123e4567-e89b-12d3-a456-426614174000"
#    When I retrieve the menu item by ID
#    Then the menu item should be retrieved successfully
#    And the response should contain the expected details
#
#  Scenario: Deleting a menu item
#    Given a tenant with ID "550e8400-e29b-41d4-a716-446655440000"
#    Given an existing menu item with ID "123e4567-e89b-12d3-a456-426614174000"
#    When I delete the menu item
#    Then the menu item should be deleted successfully
