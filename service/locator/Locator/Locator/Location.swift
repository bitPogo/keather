//
//  Location.swift
//  Locator
//
//  Created by Matthias Geisler on 23.02.24.
//

import Foundation

@objc public class Location: NSObject {
    public let longitude: Double
    public let latitude: Double
    
    public init(longitude: Double, latitude: Double) {
        self.longitude = longitude
        self.latitude = latitude
    }
}
