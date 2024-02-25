//
//  Locator.swift
//  Locator
//
//  Created by Matthias Geisler on 23.02.24.
//
import Foundation

@objc public protocol LocationResultContract {
    func success() -> AppleLocationContract?
    func error() -> NSError?
}

// object-c does not allow a proper ResultType (like Rust)
@objc public class LocationResult: NSObject, LocationResultContract {
    private var _success: AppleLocationContract?
    private var _error: NSError?
    
    private init(_ success: AppleLocationContract?, _ failure: NSError?) {
        super.init()
        self._success = success
        self._error = failure
    }
    
    public func success() -> AppleLocationContract? {
        return _success
    }
    
    public func error() -> NSError? {
        return _error
    }
    
    public convenience init(success: AppleLocationContract) {
        self.init(success, nil)
    }
    
    public convenience init(error: NSError) {
        self.init(nil, error)
    }
    
    public convenience init(_ block: () throws -> AppleLocationContract) {
        do {
            let data = try block()
            self.init(success: data)
        } catch {
            self.init(error: error as NSError)
        }
    }
}
