//
//  LocatorSpec.swift
//  UnitTests
//
//  Created by Matthias Geisler on 23.02.24.
//

import Foundation
import CoreLocation
import XCTest
import Locator

final class CLLocationManagerFake : CLLocationManager {
    let onRequestLocation: (CLLocationManager, CLLocationManagerDelegate) -> Void
    
    init(onRequestLocation: @escaping (CLLocationManager, CLLocationManagerDelegate) -> Void) {
        self.onRequestLocation = onRequestLocation
    }
    
    override func requestAlwaysAuthorization() {
        // Do nothing
    }
    
    override func requestLocation() {
        onRequestLocation(self, self.delegate!)
    }
}

final class LocatorSpec: XCTestCase {
    func testGivenTheLocationManagerFailsItPropagatesTheError() {
        // Given
        let error = NSError()
        let fake = CLLocationManagerFake {(manager, delegate) in
            delegate.locationManager?(manager, didFailWithError: error)
        }
        
        // When
        AppleLocator(locationManager: fake).locate { result in
            // Then
            XCTAssertEqual(result.error, error as NSError)
        }
    }
    
    func testGivenTheLocationManagerSucceedsItPropagatesTheSuccess() {
        // Given
        let locations = [
            CLLocation(
                latitude: CLLocationDegrees(23),
                longitude: CLLocationDegrees(21)
            )
        ]
        let fake = CLLocationManagerFake {(manager, delegate) in
            delegate.locationManager?(manager, didUpdateLocations: locations)
        }
        
        // When
        AppleLocator(locationManager: fake).locate { result in
            // Then
            let location = result.success!
            
            XCTAssertEqual(
                location.longitude,
                locations.first!.coordinate.latitude
            )
            XCTAssertEqual(
                location.longitude,
                locations.first!.coordinate.longitude
            )
        }
    }

}
