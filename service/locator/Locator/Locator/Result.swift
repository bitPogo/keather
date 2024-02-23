//
//  Locator.swift
//  Locator
//
//  Created by Matthias Geisler on 23.02.24.
//
import Foundation

// object-c does not allow a proper ResultType (like Rust)
@objc public class LocationResult: NSObject {
    
    @objc public private(set) var success: Location?
    @objc public private(set) var error: NSError?
    
    private init(_ success: Location?, _ failure: NSError?) {
        super.init()
        self.success = success
        self.error = failure
    }
    
    public convenience init(success: Location) {
        self.init(success, nil)
    }
    
    public convenience init(error: NSError) {
        self.init(nil, error)
    }
    
    public convenience init(_ block: () throws -> Location) {
        do {
            let data = try block()
            self.init(success: data)
        } catch {
            self.init(error: error as NSError)
        }
    }
}
