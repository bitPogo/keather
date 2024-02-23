//
//  Location.swift
//  Locator
//
//  Created by Matthias Geisler on 23.02.24.
//

import Foundation

@objc public protocol AppleLocationContract {
    var longitude: Double { get }
    var latitude: Double { get }
}

@objc public class AppleLocation: NSObject, AppleLocationContract {
    public let longitude: Double
    public let latitude: Double
    
    public init(longitude: Double, latitude: Double) {
        self.longitude = longitude
        self.latitude = latitude
    }
}
