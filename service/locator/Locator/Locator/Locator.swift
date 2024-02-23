//
//  Locator.swift
//  Locator
//
//  Created by Matthias Geisler on 23.02.24.
//

import Foundation
import CoreLocation

class LocatorDelegate : NSObject, CLLocationManagerDelegate {
    private var callback: (LocationResult) -> Void
    
    public convenience init(callback: @escaping (LocationResult) -> Void) {
        self.init()
        self.callback = callback
    }
    
    public override init() {
        self.callback = { (_: LocationResult) in return; }
    }
    
    
    func locationManager(
        _ manager: CLLocationManager,
        didUpdateLocations locations: [CLLocation]
    ) {
        if let location = locations.first {
            let latitude = location.coordinate.latitude
            let longitude = location.coordinate.longitude
            
            callback(
                LocationResult.init(
                    success: Location(
                        longitude: longitude,
                        latitude: latitude
                    )
                )
            )
        }
    }

    func locationManager(
        _ manager: CLLocationManager,
        didFailWithError error: Error
    ) {
        callback(LocationResult.init(error: error as NSError))
    }
}

@objc public protocol AppleLocatorContract {
    func locate(callback: @escaping (LocationResult) -> Void)
}

@objc public class AppleLocator: NSObject, AppleLocatorContract {
    private let locationManager: CLLocationManager
    
    public init(locationManager: CLLocationManager) {
        self.locationManager = locationManager
    }
    public convenience override init() {
        self.init(locationManager:  CLLocationManager())
    }
    
    public func locate(callback: @escaping (LocationResult) -> Void) {
        let delegate = LocatorDelegate(callback: callback)
        let locationManager = CLLocationManager()
        locationManager.delegate = delegate

        // Use requestAlwaysAuthorization if you need location
        // updates even when your app is running in the background
        locationManager.requestAlwaysAuthorization()
        // Request a user’s location once
        locationManager.requestLocation()
    }
}
