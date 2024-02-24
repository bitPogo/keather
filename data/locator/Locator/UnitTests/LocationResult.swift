//
//  UnitTests.swift
//  UnitTests
//
//  Created by Matthias Geisler on 23.02.24.
//

import XCTest
import Locator

final class ResultSpec: XCTestCase {
    func testGivenDataIsEmittedItWrapsIt() throws {
        // Given
        let error = NSError()
        
        // When
        
        let result = LocationResult {
            throw error
        }
        
        // Then
        XCTAssertEqual(result.error(), error as NSError)
    }
    
    func testGivenAnErrorIsThrownItWrapsIt() throws {
        // Given
        let data = AppleLocation(longitude: 24, latitude: 23)
        
        // When
        let result = LocationResult {
            return data
        }
        
        // Then
        XCTAssertEqual(result.success()?.latitude, data.latitude)
        XCTAssertEqual(result.success()?.longitude, data.longitude)
    }
}
