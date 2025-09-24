#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class AephyrSharedDashboardData, AephyrSharedDashboardUi, AephyrSharedFactories, AephyrSharedFood, AephyrSharedFoodCompanion, AephyrSharedFoodDto, AephyrSharedFoodDtoCompanion, AephyrSharedFoodItem, AephyrSharedFoodItemCompanion, AephyrSharedHero, AephyrSharedK_AggregateHero, AephyrSharedK_Aggregates, AephyrSharedK_DietMoment, AephyrSharedK_Energy, AephyrSharedK_EnergyCompanion, AephyrSharedK_EnergyUnit, AephyrSharedK_EnergyUnitCompanion, AephyrSharedK_FoodStore, AephyrSharedK_LoggedFood, AephyrSharedK_Macros, AephyrSharedK_MacrosCompanion, AephyrSharedK_Mass, AephyrSharedK_MassCompanion, AephyrSharedK_MassUnit, AephyrSharedK_MassUnitCompanion, AephyrSharedK_NewLogInput, AephyrSharedK_Notice, AephyrSharedK_NoticeSeverity, AephyrSharedK_QuantityMass, AephyrSharedKotlinArray<T>, AephyrSharedKotlinEnum<E>, AephyrSharedKotlinEnumCompanion, AephyrSharedKotlinException, AephyrSharedKotlinIllegalStateException, AephyrSharedKotlinNothing, AephyrSharedKotlinRuntimeException, AephyrSharedKotlinThrowable, AephyrSharedKotlinx_datetimeDayOfWeek, AephyrSharedKotlinx_datetimeDayOfWeekNames, AephyrSharedKotlinx_datetimeDayOfWeekNamesCompanion, AephyrSharedKotlinx_datetimeInstant, AephyrSharedKotlinx_datetimeInstantCompanion, AephyrSharedKotlinx_datetimeLocalDate, AephyrSharedKotlinx_datetimeLocalDateCompanion, AephyrSharedKotlinx_datetimeMonth, AephyrSharedKotlinx_datetimeMonthNames, AephyrSharedKotlinx_datetimeMonthNamesCompanion, AephyrSharedKotlinx_datetimePadding, AephyrSharedKotlinx_serialization_coreSerialKind, AephyrSharedKotlinx_serialization_coreSerializersModule, AephyrSharedMockFoodStore, AephyrSharedMockFoodStoreCompanion, AephyrSharedNutrients, AephyrSharedNutrientsCompanion, AephyrSharedNutrientsDto, AephyrSharedNutrientsDtoCompanion, AephyrSharedNutriments, AephyrSharedNutrimentsCompanion, AephyrSharedNutrimentsV1, AephyrSharedNutrimentsV1Companion, AephyrSharedOffProduct, AephyrSharedOffProductCompanion, AephyrSharedOffProductResponse, AephyrSharedOffProductResponseCompanion, AephyrSharedOffProductV1, AephyrSharedOffProductV1Companion, AephyrSharedOffSearchV1Response, AephyrSharedOffSearchV1ResponseCompanion, AephyrSharedSearchResponse, AephyrSharedSearchResponseCompanion;

@protocol AephyrSharedDashboardRepository, AephyrSharedFoodService, AephyrSharedFoodStore, AephyrSharedK_Cancellable, AephyrSharedK_Quantity, AephyrSharedKotlinAnnotation, AephyrSharedKotlinAppendable, AephyrSharedKotlinComparable, AephyrSharedKotlinCoroutineContext, AephyrSharedKotlinCoroutineContextElement, AephyrSharedKotlinCoroutineContextKey, AephyrSharedKotlinIterator, AephyrSharedKotlinKAnnotatedElement, AephyrSharedKotlinKClass, AephyrSharedKotlinKClassifier, AephyrSharedKotlinKDeclarationContainer, AephyrSharedKotlinx_coroutines_coreCoroutineScope, AephyrSharedKotlinx_coroutines_coreFlow, AephyrSharedKotlinx_coroutines_coreFlowCollector, AephyrSharedKotlinx_coroutines_coreSharedFlow, AephyrSharedKotlinx_coroutines_coreStateFlow, AephyrSharedKotlinx_datetimeDateTimeFormat, AephyrSharedKotlinx_datetimeDateTimeFormatBuilder, AephyrSharedKotlinx_datetimeDateTimeFormatBuilderWithDate, AephyrSharedKotlinx_serialization_coreCompositeDecoder, AephyrSharedKotlinx_serialization_coreCompositeEncoder, AephyrSharedKotlinx_serialization_coreDecoder, AephyrSharedKotlinx_serialization_coreDeserializationStrategy, AephyrSharedKotlinx_serialization_coreEncoder, AephyrSharedKotlinx_serialization_coreKSerializer, AephyrSharedKotlinx_serialization_coreSerialDescriptor, AephyrSharedKotlinx_serialization_coreSerializationStrategy, AephyrSharedKotlinx_serialization_coreSerializersModuleCollector;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

#pragma push_macro("_Nullable_result")
#if !__has_feature(nullability_nullable_result)
#undef _Nullable_result
#define _Nullable_result _Nullable
#endif

__attribute__((swift_name("KotlinBase")))
@interface AephyrSharedBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end

@interface AephyrSharedBase (AephyrSharedBaseCopying) <NSCopying>
@end

__attribute__((swift_name("KotlinMutableSet")))
@interface AephyrSharedMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end

__attribute__((swift_name("KotlinMutableDictionary")))
@interface AephyrSharedMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end

@interface NSError (NSErrorAephyrSharedKotlinException)
@property (readonly) id _Nullable kotlinException;
@end

__attribute__((swift_name("KotlinNumber")))
@interface AephyrSharedNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end

__attribute__((swift_name("KotlinByte")))
@interface AephyrSharedByte : AephyrSharedNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end

__attribute__((swift_name("KotlinUByte")))
@interface AephyrSharedUByte : AephyrSharedNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end

__attribute__((swift_name("KotlinShort")))
@interface AephyrSharedShort : AephyrSharedNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end

__attribute__((swift_name("KotlinUShort")))
@interface AephyrSharedUShort : AephyrSharedNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end

__attribute__((swift_name("KotlinInt")))
@interface AephyrSharedInt : AephyrSharedNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end

__attribute__((swift_name("KotlinUInt")))
@interface AephyrSharedUInt : AephyrSharedNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end

__attribute__((swift_name("KotlinLong")))
@interface AephyrSharedLong : AephyrSharedNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end

__attribute__((swift_name("KotlinULong")))
@interface AephyrSharedULong : AephyrSharedNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end

__attribute__((swift_name("KotlinFloat")))
@interface AephyrSharedFloat : AephyrSharedNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end

__attribute__((swift_name("KotlinDouble")))
@interface AephyrSharedDouble : AephyrSharedNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end

__attribute__((swift_name("KotlinBoolean")))
@interface AephyrSharedBoolean : AephyrSharedNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end

__attribute__((swift_name("FoodStore")))
@protocol AephyrSharedFoodStore
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)addName:(NSString *)name mass:(AephyrSharedK_Mass * _Nullable)mass energy:(AephyrSharedK_Energy * _Nullable)energy macros:(AephyrSharedK_Macros * _Nullable)macros completionHandler:(void (^)(AephyrSharedFoodItem * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("add(name:mass:energy:macros:completionHandler:)")));
- (id<AephyrSharedKotlinx_coroutines_coreFlow>)observeToday __attribute__((swift_name("observeToday()")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)removeId:(NSString *)id completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("remove(id:completionHandler:)")));
@property (readonly) id<AephyrSharedKotlinx_coroutines_coreStateFlow> today __attribute__((swift_name("today")));
@end

__attribute__((swift_name("K_Cancellable")))
@protocol AephyrSharedK_Cancellable
@required
- (void)cancel __attribute__((swift_name("cancel()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FoodDto")))
@interface AephyrSharedFoodDto : AephyrSharedBase
- (instancetype)initWithId:(NSString *)id barcode:(NSString * _Nullable)barcode name:(NSString *)name brand:(NSString * _Nullable)brand per100g:(AephyrSharedNutrientsDto *)per100g verified:(BOOL)verified __attribute__((swift_name("init(id:barcode:name:brand:per100g:verified:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedFoodDtoCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedFoodDto *)doCopyId:(NSString *)id barcode:(NSString * _Nullable)barcode name:(NSString *)name brand:(NSString * _Nullable)brand per100g:(AephyrSharedNutrientsDto *)per100g verified:(BOOL)verified __attribute__((swift_name("doCopy(id:barcode:name:brand:per100g:verified:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable barcode __attribute__((swift_name("barcode")));
@property (readonly) NSString * _Nullable brand __attribute__((swift_name("brand")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) AephyrSharedNutrientsDto *per100g __attribute__((swift_name("per100g")));
@property (readonly) BOOL verified __attribute__((swift_name("verified")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FoodDto.Companion")))
@interface AephyrSharedFoodDtoCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedFoodDtoCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NutrientsDto")))
@interface AephyrSharedNutrientsDto : AephyrSharedBase
- (instancetype)initWithKcal:(double)kcal protein:(double)protein carbs:(double)carbs fat:(double)fat __attribute__((swift_name("init(kcal:protein:carbs:fat:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedNutrientsDtoCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedNutrientsDto *)doCopyKcal:(double)kcal protein:(double)protein carbs:(double)carbs fat:(double)fat __attribute__((swift_name("doCopy(kcal:protein:carbs:fat:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double carbs __attribute__((swift_name("carbs")));
@property (readonly) double fat __attribute__((swift_name("fat")));
@property (readonly) double kcal __attribute__((swift_name("kcal")));
@property (readonly) double protein __attribute__((swift_name("protein")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NutrientsDto.Companion")))
@interface AephyrSharedNutrientsDtoCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedNutrientsDtoCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((swift_name("FoodService")))
@protocol AephyrSharedFoodService
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)byBarcodeCode:(NSString *)code completionHandler:(void (^)(AephyrSharedFood * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("byBarcode(code:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)searchQuery:(NSString *)query completionHandler:(void (^)(NSArray<AephyrSharedFood *> * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("search(query:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("HttpFoodService")))
@interface AephyrSharedHttpFoodService : AephyrSharedBase <AephyrSharedFoodService>
- (instancetype)initWithBaseUrl:(NSString *)baseUrl userAgent:(NSString *)userAgent __attribute__((swift_name("init(baseUrl:userAgent:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)byBarcodeCode:(NSString *)code completionHandler:(void (^)(AephyrSharedFood * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("byBarcode(code:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)searchQuery:(NSString *)query completionHandler:(void (^)(NSArray<AephyrSharedFood *> * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("search(query:completionHandler:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Nutriments")))
@interface AephyrSharedNutriments : AephyrSharedBase
- (instancetype)initWithKcal:(AephyrSharedDouble * _Nullable)kcal protein100g:(AephyrSharedDouble * _Nullable)protein100g carbs100g:(AephyrSharedDouble * _Nullable)carbs100g fat100g:(AephyrSharedDouble * _Nullable)fat100g __attribute__((swift_name("init(kcal:protein100g:carbs100g:fat100g:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedNutrimentsCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedNutriments *)doCopyKcal:(AephyrSharedDouble * _Nullable)kcal protein100g:(AephyrSharedDouble * _Nullable)protein100g carbs100g:(AephyrSharedDouble * _Nullable)carbs100g fat100g:(AephyrSharedDouble * _Nullable)fat100g __attribute__((swift_name("doCopy(kcal:protein100g:carbs100g:fat100g:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="carbohydrates_100g")
*/
@property (readonly) AephyrSharedDouble * _Nullable carbs100g __attribute__((swift_name("carbs100g")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="fat_100g")
*/
@property (readonly) AephyrSharedDouble * _Nullable fat100g __attribute__((swift_name("fat100g")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="energy-kcal_100g")
*/
@property (readonly) AephyrSharedDouble * _Nullable kcal __attribute__((swift_name("kcal")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="proteins_100g")
*/
@property (readonly) AephyrSharedDouble * _Nullable protein100g __attribute__((swift_name("protein100g")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Nutriments.Companion")))
@interface AephyrSharedNutrimentsCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedNutrimentsCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NutrimentsV1")))
@interface AephyrSharedNutrimentsV1 : AephyrSharedBase
- (instancetype)initWithKcal:(AephyrSharedDouble * _Nullable)kcal protein100g:(AephyrSharedDouble * _Nullable)protein100g carbs100g:(AephyrSharedDouble * _Nullable)carbs100g fat100g:(AephyrSharedDouble * _Nullable)fat100g __attribute__((swift_name("init(kcal:protein100g:carbs100g:fat100g:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedNutrimentsV1Companion *companion __attribute__((swift_name("companion")));
- (AephyrSharedNutrimentsV1 *)doCopyKcal:(AephyrSharedDouble * _Nullable)kcal protein100g:(AephyrSharedDouble * _Nullable)protein100g carbs100g:(AephyrSharedDouble * _Nullable)carbs100g fat100g:(AephyrSharedDouble * _Nullable)fat100g __attribute__((swift_name("doCopy(kcal:protein100g:carbs100g:fat100g:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="carbohydrates_100g")
*/
@property (readonly) AephyrSharedDouble * _Nullable carbs100g __attribute__((swift_name("carbs100g")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="fat_100g")
*/
@property (readonly) AephyrSharedDouble * _Nullable fat100g __attribute__((swift_name("fat100g")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="energy-kcal_100g")
*/
@property (readonly) AephyrSharedDouble * _Nullable kcal __attribute__((swift_name("kcal")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="proteins_100g")
*/
@property (readonly) AephyrSharedDouble * _Nullable protein100g __attribute__((swift_name("protein100g")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("NutrimentsV1.Companion")))
@interface AephyrSharedNutrimentsV1Companion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedNutrimentsV1Companion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OffFoodService")))
@interface AephyrSharedOffFoodService : AephyrSharedBase <AephyrSharedFoodService>
- (instancetype)initWithStaging:(BOOL)staging __attribute__((swift_name("init(staging:)"))) __attribute__((objc_designated_initializer));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)byBarcodeCode:(NSString *)code completionHandler:(void (^)(AephyrSharedFood * _Nullable_result, NSError * _Nullable))completionHandler __attribute__((swift_name("byBarcode(code:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)searchQuery:(NSString *)query completionHandler:(void (^)(NSArray<AephyrSharedFood *> * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("search(query:completionHandler:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OffProduct")))
@interface AephyrSharedOffProduct : AephyrSharedBase
- (instancetype)initWithCode:(NSString * _Nullable)code productName:(NSString * _Nullable)productName brands:(NSString * _Nullable)brands nutriments:(AephyrSharedNutriments * _Nullable)nutriments __attribute__((swift_name("init(code:productName:brands:nutriments:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedOffProductCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedOffProduct *)doCopyCode:(NSString * _Nullable)code productName:(NSString * _Nullable)productName brands:(NSString * _Nullable)brands nutriments:(AephyrSharedNutriments * _Nullable)nutriments __attribute__((swift_name("doCopy(code:productName:brands:nutriments:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="brands")
*/
@property (readonly) NSString * _Nullable brands __attribute__((swift_name("brands")));
@property (readonly) NSString * _Nullable code __attribute__((swift_name("code")));
@property (readonly) AephyrSharedNutriments * _Nullable nutriments __attribute__((swift_name("nutriments")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="product_name")
*/
@property (readonly) NSString * _Nullable productName __attribute__((swift_name("productName")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OffProduct.Companion")))
@interface AephyrSharedOffProductCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedOffProductCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OffProductResponse")))
@interface AephyrSharedOffProductResponse : AephyrSharedBase
- (instancetype)initWithCode:(NSString *)code status:(AephyrSharedInt * _Nullable)status statusVerbose:(NSString * _Nullable)statusVerbose product:(AephyrSharedOffProduct * _Nullable)product __attribute__((swift_name("init(code:status:statusVerbose:product:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedOffProductResponseCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedOffProductResponse *)doCopyCode:(NSString *)code status:(AephyrSharedInt * _Nullable)status statusVerbose:(NSString * _Nullable)statusVerbose product:(AephyrSharedOffProduct * _Nullable)product __attribute__((swift_name("doCopy(code:status:statusVerbose:product:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *code __attribute__((swift_name("code")));
@property (readonly) AephyrSharedOffProduct * _Nullable product __attribute__((swift_name("product")));
@property (readonly) AephyrSharedInt * _Nullable status __attribute__((swift_name("status")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="status_verbose")
*/
@property (readonly) NSString * _Nullable statusVerbose __attribute__((swift_name("statusVerbose")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OffProductResponse.Companion")))
@interface AephyrSharedOffProductResponseCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedOffProductResponseCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OffProductV1")))
@interface AephyrSharedOffProductV1 : AephyrSharedBase
- (instancetype)initWithCode:(NSString * _Nullable)code productName:(NSString * _Nullable)productName brands:(NSString * _Nullable)brands nutriments:(AephyrSharedNutrimentsV1 * _Nullable)nutriments __attribute__((swift_name("init(code:productName:brands:nutriments:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedOffProductV1Companion *companion __attribute__((swift_name("companion")));
- (AephyrSharedOffProductV1 *)doCopyCode:(NSString * _Nullable)code productName:(NSString * _Nullable)productName brands:(NSString * _Nullable)brands nutriments:(AephyrSharedNutrimentsV1 * _Nullable)nutriments __attribute__((swift_name("doCopy(code:productName:brands:nutriments:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable brands __attribute__((swift_name("brands")));
@property (readonly) NSString * _Nullable code __attribute__((swift_name("code")));
@property (readonly) AephyrSharedNutrimentsV1 * _Nullable nutriments __attribute__((swift_name("nutriments")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="product_name")
*/
@property (readonly) NSString * _Nullable productName __attribute__((swift_name("productName")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OffProductV1.Companion")))
@interface AephyrSharedOffProductV1Companion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedOffProductV1Companion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OffSearchV1Response")))
@interface AephyrSharedOffSearchV1Response : AephyrSharedBase
- (instancetype)initWithCount:(int32_t)count page:(int32_t)page pageSize:(int32_t)pageSize products:(NSArray<AephyrSharedOffProductV1 *> *)products __attribute__((swift_name("init(count:page:pageSize:products:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedOffSearchV1ResponseCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedOffSearchV1Response *)doCopyCount:(int32_t)count page:(int32_t)page pageSize:(int32_t)pageSize products:(NSArray<AephyrSharedOffProductV1 *> *)products __attribute__((swift_name("doCopy(count:page:pageSize:products:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t count __attribute__((swift_name("count")));
@property (readonly) int32_t page __attribute__((swift_name("page")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="page_size")
*/
@property (readonly) int32_t pageSize __attribute__((swift_name("pageSize")));
@property (readonly) NSArray<AephyrSharedOffProductV1 *> *products __attribute__((swift_name("products")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("OffSearchV1Response.Companion")))
@interface AephyrSharedOffSearchV1ResponseCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedOffSearchV1ResponseCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SearchResponse")))
@interface AephyrSharedSearchResponse : AephyrSharedBase
- (instancetype)initWithCount:(int32_t)count page:(int32_t)page pageCount:(int32_t)pageCount pageSize:(int32_t)pageSize products:(NSArray<AephyrSharedOffProduct *> *)products __attribute__((swift_name("init(count:page:pageCount:pageSize:products:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedSearchResponseCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedSearchResponse *)doCopyCount:(int32_t)count page:(int32_t)page pageCount:(int32_t)pageCount pageSize:(int32_t)pageSize products:(NSArray<AephyrSharedOffProduct *> *)products __attribute__((swift_name("doCopy(count:page:pageCount:pageSize:products:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t count __attribute__((swift_name("count")));
@property (readonly) int32_t page __attribute__((swift_name("page")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="page_count")
*/
@property (readonly) int32_t pageCount __attribute__((swift_name("pageCount")));

/**
 * @note annotations
 *   kotlinx.serialization.SerialName(value="page_size")
*/
@property (readonly) int32_t pageSize __attribute__((swift_name("pageSize")));
@property (readonly) NSArray<AephyrSharedOffProduct *> *products __attribute__((swift_name("products")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("SearchResponse.Companion")))
@interface AephyrSharedSearchResponseCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedSearchResponseCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Factories")))
@interface AephyrSharedFactories : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)factories __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedFactories *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedFoodService>)foodService __attribute__((swift_name("foodService()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Food")))
@interface AephyrSharedFood : AephyrSharedBase
- (instancetype)initWithId:(NSString *)id barcode:(NSString * _Nullable)barcode name:(NSString *)name brand:(NSString * _Nullable)brand per100g:(AephyrSharedNutrients *)per100g verified:(BOOL)verified __attribute__((swift_name("init(id:barcode:name:brand:per100g:verified:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedFoodCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedFood *)doCopyId:(NSString *)id barcode:(NSString * _Nullable)barcode name:(NSString *)name brand:(NSString * _Nullable)brand per100g:(AephyrSharedNutrients *)per100g verified:(BOOL)verified __attribute__((swift_name("doCopy(id:barcode:name:brand:per100g:verified:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable barcode __attribute__((swift_name("barcode")));
@property (readonly) NSString * _Nullable brand __attribute__((swift_name("brand")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) AephyrSharedNutrients *per100g __attribute__((swift_name("per100g")));
@property (readonly) BOOL verified __attribute__((swift_name("verified")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Food.Companion")))
@interface AephyrSharedFoodCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedFoodCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Nutrients")))
@interface AephyrSharedNutrients : AephyrSharedBase
- (instancetype)initWithKcal:(double)kcal protein:(double)protein carbs:(double)carbs fat:(double)fat __attribute__((swift_name("init(kcal:protein:carbs:fat:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedNutrientsCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedNutrients *)doCopyKcal:(double)kcal protein:(double)protein carbs:(double)carbs fat:(double)fat __attribute__((swift_name("doCopy(kcal:protein:carbs:fat:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double carbs __attribute__((swift_name("carbs")));
@property (readonly) double fat __attribute__((swift_name("fat")));
@property (readonly) double kcal __attribute__((swift_name("kcal")));
@property (readonly) double protein __attribute__((swift_name("protein")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Nutrients.Companion")))
@interface AephyrSharedNutrientsCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedNutrientsCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DashboardData")))
@interface AephyrSharedDashboardData : AephyrSharedBase
- (instancetype)initWithHero:(AephyrSharedHero * _Nullable)hero entries:(NSArray<AephyrSharedFoodItem *> *)entries __attribute__((swift_name("init(hero:entries:)"))) __attribute__((objc_designated_initializer));
- (AephyrSharedDashboardData *)doCopyHero:(AephyrSharedHero * _Nullable)hero entries:(NSArray<AephyrSharedFoodItem *> *)entries __attribute__((swift_name("doCopy(hero:entries:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<AephyrSharedFoodItem *> *entries __attribute__((swift_name("entries")));
@property (readonly) AephyrSharedHero * _Nullable hero __attribute__((swift_name("hero")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DashboardFacade")))
@interface AephyrSharedDashboardFacade : AephyrSharedBase
- (instancetype)initWithRepo:(id<AephyrSharedDashboardRepository>)repo __attribute__((swift_name("init(repo:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithRepo:(id<AephyrSharedDashboardRepository>)repo scope:(id<AephyrSharedKotlinx_coroutines_coreCoroutineScope>)scope __attribute__((swift_name("init(repo:scope:)"))) __attribute__((objc_designated_initializer));
- (id<AephyrSharedKotlinx_coroutines_coreFlow>)observeState __attribute__((swift_name("observeState()")));

/** One-shot load for "today".
 *
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)refreshWithCompletionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("refresh(completionHandler:)")));

/** Remove an item and refresh list.
 *
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)removeId:(NSString *)id completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("remove(id:completionHandler:)")));
@property (readonly) id<AephyrSharedKotlinx_coroutines_coreStateFlow> state __attribute__((swift_name("state")));
@end

__attribute__((swift_name("DashboardRepository")))
@protocol AephyrSharedDashboardRepository
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)fetchTodayWithCompletionHandler:(void (^)(AephyrSharedDashboardData * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("fetchToday(completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)removeId:(NSString *)id completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("remove(id:completionHandler:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("DashboardUi")))
@interface AephyrSharedDashboardUi : AephyrSharedBase
- (instancetype)initWithIsLoading:(BOOL)isLoading hero:(AephyrSharedHero * _Nullable)hero entries:(NSArray<AephyrSharedFoodItem *> *)entries __attribute__((swift_name("init(isLoading:hero:entries:)"))) __attribute__((objc_designated_initializer));
- (AephyrSharedDashboardUi *)doCopyIsLoading:(BOOL)isLoading hero:(AephyrSharedHero * _Nullable)hero entries:(NSArray<AephyrSharedFoodItem *> *)entries __attribute__((swift_name("doCopy(isLoading:hero:entries:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<AephyrSharedFoodItem *> *entries __attribute__((swift_name("entries")));
@property (readonly) AephyrSharedHero * _Nullable hero __attribute__((swift_name("hero")));
@property (readonly) BOOL isLoading __attribute__((swift_name("isLoading")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FoodItem")))
@interface AephyrSharedFoodItem : AephyrSharedBase
- (instancetype)initWithId:(NSString *)id name:(NSString *)name mass:(AephyrSharedK_Mass * _Nullable)mass energy:(AephyrSharedK_Energy * _Nullable)energy macros:(AephyrSharedK_Macros * _Nullable)macros __attribute__((swift_name("init(id:name:mass:energy:macros:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedFoodItemCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedFoodItem *)doCopyId:(NSString *)id name:(NSString *)name mass:(AephyrSharedK_Mass * _Nullable)mass energy:(AephyrSharedK_Energy * _Nullable)energy macros:(AephyrSharedK_Macros * _Nullable)macros __attribute__((swift_name("doCopy(id:name:mass:energy:macros:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AephyrSharedK_Energy * _Nullable energy __attribute__((swift_name("energy")));
@property (readonly) NSString *id __attribute__((swift_name("id")));
@property (readonly) AephyrSharedK_Macros * _Nullable macros __attribute__((swift_name("macros")));
@property (readonly) AephyrSharedK_Mass * _Nullable mass __attribute__((swift_name("mass")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("FoodItem.Companion")))
@interface AephyrSharedFoodItemCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedFoodItemCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Hero")))
@interface AephyrSharedHero : AephyrSharedBase
- (instancetype)initWithTitle:(NSString *)title subtitle:(NSString * _Nullable)subtitle __attribute__((swift_name("init(title:subtitle:)"))) __attribute__((objc_designated_initializer));
- (AephyrSharedHero *)doCopyTitle:(NSString *)title subtitle:(NSString * _Nullable)subtitle __attribute__((swift_name("doCopy(title:subtitle:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString * _Nullable subtitle __attribute__((swift_name("subtitle")));
@property (readonly) NSString *title __attribute__((swift_name("title")));
@end


/**
 * Swift-friendly facade over K_FoodStore.
 * - Keeps suspend methods as-is (Swift gets completion handlers automatically).
 * - Exposes Flow<StateFlow) as a "watch" API that returns a cancellable.
 *
 * NOTE: Anything used from Swift should be prefixed `K_` per your convention.
 * Ensure your models are named accordingly (e.g., K_FoodItem, K_Mass, ...).
 */
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_FoodStore")))
@interface AephyrSharedK_FoodStore : AephyrSharedBase
- (instancetype)initWithStore:(id<AephyrSharedFoodStore>)store scope:(id<AephyrSharedKotlinx_coroutines_coreCoroutineScope>)scope __attribute__((swift_name("init(store:scope:)"))) __attribute__((objc_designated_initializer));

/** Stream today's items with main-thread emissions for UI. */
- (id<AephyrSharedK_Cancellable>)watchTodayOnEach:(void (^)(NSArray<AephyrSharedFoodItem *> *))onEach __attribute__((swift_name("watchToday(onEach:)")));

/** If you also want to expose the Flow version without @NativeCoroutines: */
- (id<AephyrSharedK_Cancellable>)watchTodayFromFlowOnEach:(void (^)(NSArray<AephyrSharedFoodItem *> *))onEach __attribute__((swift_name("watchTodayFromFlow(onEach:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_FoodStoreFactory")))
@interface AephyrSharedK_FoodStoreFactory : AephyrSharedBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (AephyrSharedK_FoodStore *)instance __attribute__((swift_name("instance()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MockFoodStore")))
@interface AephyrSharedMockFoodStore : AephyrSharedBase <AephyrSharedFoodStore>
- (instancetype)initWithSeed:(NSArray<AephyrSharedFoodItem *> *)seed __attribute__((swift_name("init(seed:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedMockFoodStoreCompanion *companion __attribute__((swift_name("companion")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)addName:(NSString *)name mass:(AephyrSharedK_Mass * _Nullable)mass energy:(AephyrSharedK_Energy * _Nullable)energy macros:(AephyrSharedK_Macros * _Nullable)macros completionHandler:(void (^)(AephyrSharedFoodItem * _Nullable, NSError * _Nullable))completionHandler __attribute__((swift_name("add(name:mass:energy:macros:completionHandler:)")));

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)removeId:(NSString *)id completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("remove(id:completionHandler:)")));
@property (readonly) id<AephyrSharedKotlinx_coroutines_coreStateFlow> today __attribute__((swift_name("today")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("MockFoodStore.Companion")))
@interface AephyrSharedMockFoodStoreCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedMockFoodStoreCompanion *shared __attribute__((swift_name("shared")));
- (AephyrSharedMockFoodStore *)withSeeds __attribute__((swift_name("withSeeds()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_AggregateHero")))
@interface AephyrSharedK_AggregateHero : AephyrSharedBase
- (instancetype)initWithDay:(AephyrSharedKotlinx_datetimeLocalDate *)day totalEnergy:(AephyrSharedK_Energy *)totalEnergy totalMacros:(AephyrSharedK_Macros *)totalMacros proteinPct:(double)proteinPct fatPct:(double)fatPct carbPct:(double)carbPct __attribute__((swift_name("init(day:totalEnergy:totalMacros:proteinPct:fatPct:carbPct:)"))) __attribute__((objc_designated_initializer));
- (AephyrSharedK_AggregateHero *)doCopyDay:(AephyrSharedKotlinx_datetimeLocalDate *)day totalEnergy:(AephyrSharedK_Energy *)totalEnergy totalMacros:(AephyrSharedK_Macros *)totalMacros proteinPct:(double)proteinPct fatPct:(double)fatPct carbPct:(double)carbPct __attribute__((swift_name("doCopy(day:totalEnergy:totalMacros:proteinPct:fatPct:carbPct:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) double carbPct __attribute__((swift_name("carbPct")));
@property (readonly) AephyrSharedKotlinx_datetimeLocalDate *day __attribute__((swift_name("day")));
@property (readonly) double fatPct __attribute__((swift_name("fatPct")));
@property (readonly) double proteinPct __attribute__((swift_name("proteinPct")));
@property (readonly) AephyrSharedK_Energy *totalEnergy __attribute__((swift_name("totalEnergy")));
@property (readonly) AephyrSharedK_Macros *totalMacros __attribute__((swift_name("totalMacros")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_Aggregates")))
@interface AephyrSharedK_Aggregates : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)k_Aggregates __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedK_Aggregates *shared __attribute__((swift_name("shared")));

/** Build the dashboard hero for a day. */
- (AephyrSharedK_AggregateHero *)heroDay:(AephyrSharedKotlinx_datetimeLocalDate *)day entries:(NSArray<AephyrSharedK_LoggedFood *> *)entries roundingKcalStep:(int32_t)roundingKcalStep __attribute__((swift_name("hero(day:entries:roundingKcalStep:)")));
@end

__attribute__((swift_name("KotlinComparable")))
@protocol AephyrSharedKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_DietMoment")))
@interface AephyrSharedK_DietMoment : AephyrSharedBase <AephyrSharedKotlinComparable>
- (instancetype)initWithDay:(AephyrSharedKotlinx_datetimeLocalDate *)day timeMinutes:(AephyrSharedInt * _Nullable)timeMinutes __attribute__((swift_name("init(day:timeMinutes:)"))) __attribute__((objc_designated_initializer));
- (int32_t)compareToOther:(AephyrSharedK_DietMoment *)other __attribute__((swift_name("compareTo(other:)")));
- (AephyrSharedK_DietMoment *)doCopyDay:(AephyrSharedKotlinx_datetimeLocalDate *)day timeMinutes:(AephyrSharedInt * _Nullable)timeMinutes __attribute__((swift_name("doCopy(day:timeMinutes:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (BOOL)isSameDayOther:(AephyrSharedK_DietMoment *)other __attribute__((swift_name("isSameDay(other:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AephyrSharedKotlinx_datetimeLocalDate *day __attribute__((swift_name("day")));
@property (readonly) AephyrSharedInt * _Nullable timeMinutes __attribute__((swift_name("timeMinutes")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_LoggedFood")))
@interface AephyrSharedK_LoggedFood : AephyrSharedBase <AephyrSharedKotlinComparable>
- (instancetype)initWithId:(id)id consumedAt:(AephyrSharedK_DietMoment *)consumedAt name:(id)name portion:(AephyrSharedK_Mass * _Nullable)portion energy:(AephyrSharedK_Energy * _Nullable)energy macros:(AephyrSharedK_Macros * _Nullable)macros notices:(NSArray<AephyrSharedK_Notice *> *)notices loggedAt:(AephyrSharedKotlinx_datetimeInstant *)loggedAt __attribute__((swift_name("init(id:consumedAt:name:portion:energy:macros:notices:loggedAt:)"))) __attribute__((objc_designated_initializer));
- (int32_t)compareToOther:(AephyrSharedK_LoggedFood *)other __attribute__((swift_name("compareTo(other:)")));
- (AephyrSharedK_LoggedFood *)doCopyId:(id)id consumedAt:(AephyrSharedK_DietMoment *)consumedAt name:(id)name portion:(AephyrSharedK_Mass * _Nullable)portion energy:(AephyrSharedK_Energy * _Nullable)energy macros:(AephyrSharedK_Macros * _Nullable)macros notices:(NSArray<AephyrSharedK_Notice *> *)notices loggedAt:(AephyrSharedKotlinx_datetimeInstant *)loggedAt __attribute__((swift_name("doCopy(id:consumedAt:name:portion:energy:macros:notices:loggedAt:)")));
- (AephyrSharedK_Energy * _Nullable)energyBestEffort __attribute__((swift_name("energyBestEffort()")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AephyrSharedK_DietMoment *consumedAt __attribute__((swift_name("consumedAt")));
@property (readonly) AephyrSharedK_Energy * _Nullable energy __attribute__((swift_name("energy")));
@property (readonly) id id __attribute__((swift_name("id")));
@property (readonly) AephyrSharedKotlinx_datetimeInstant *loggedAt __attribute__((swift_name("loggedAt")));
@property (readonly) AephyrSharedK_Macros * _Nullable macros __attribute__((swift_name("macros")));
@property (readonly) id name __attribute__((swift_name("name")));
@property (readonly) NSArray<AephyrSharedK_Notice *> *notices __attribute__((swift_name("notices")));
@property (readonly) AephyrSharedK_Mass * _Nullable portion __attribute__((swift_name("portion")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_Macros")))
@interface AephyrSharedK_Macros : AephyrSharedBase
- (instancetype)initWithProtein:(AephyrSharedK_Mass *)protein fat:(AephyrSharedK_Mass *)fat carb:(AephyrSharedK_Mass *)carb __attribute__((swift_name("init(protein:fat:carb:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedK_MacrosCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedK_Macros *)doCopyProtein:(AephyrSharedK_Mass *)protein fat:(AephyrSharedK_Mass *)fat carb:(AephyrSharedK_Mass *)carb __attribute__((swift_name("doCopy(protein:fat:carb:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (AephyrSharedK_Macros *)plusO:(AephyrSharedK_Macros *)o __attribute__((swift_name("plus(o:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AephyrSharedK_Mass *carb __attribute__((swift_name("carb")));
@property (readonly) AephyrSharedK_Energy *carbEnergy __attribute__((swift_name("carbEnergy")));
@property (readonly) AephyrSharedK_Mass *fat __attribute__((swift_name("fat")));
@property (readonly) AephyrSharedK_Energy *fatEnergy __attribute__((swift_name("fatEnergy")));
@property (readonly) AephyrSharedK_Mass *protein __attribute__((swift_name("protein")));
@property (readonly) AephyrSharedK_Energy *proteinEnergy __attribute__((swift_name("proteinEnergy")));
@property (readonly) AephyrSharedK_Energy *totalEnergy __attribute__((swift_name("totalEnergy")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_Macros.Companion")))
@interface AephyrSharedK_MacrosCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedK_MacrosCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) AephyrSharedK_Macros *ZERO __attribute__((swift_name("ZERO")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_NewLogInput")))
@interface AephyrSharedK_NewLogInput : AephyrSharedBase
- (instancetype)initWithConsumedAt:(AephyrSharedK_DietMoment *)consumedAt name:(id)name portion:(id<AephyrSharedK_Quantity> _Nullable)portion energy:(AephyrSharedK_Energy * _Nullable)energy macros:(AephyrSharedK_Macros * _Nullable)macros __attribute__((swift_name("init(consumedAt:name:portion:energy:macros:)"))) __attribute__((objc_designated_initializer));
- (AephyrSharedK_NewLogInput *)doCopyConsumedAt:(AephyrSharedK_DietMoment *)consumedAt name:(id)name portion:(id<AephyrSharedK_Quantity> _Nullable)portion energy:(AephyrSharedK_Energy * _Nullable)energy macros:(AephyrSharedK_Macros * _Nullable)macros __attribute__((swift_name("doCopy(consumedAt:name:portion:energy:macros:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AephyrSharedK_DietMoment *consumedAt __attribute__((swift_name("consumedAt")));
@property (readonly) AephyrSharedK_Energy * _Nullable energy __attribute__((swift_name("energy")));
@property (readonly) AephyrSharedK_Macros * _Nullable macros __attribute__((swift_name("macros")));
@property (readonly) id name __attribute__((swift_name("name")));
@property (readonly) id<AephyrSharedK_Quantity> _Nullable portion __attribute__((swift_name("portion")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_Notice")))
@interface AephyrSharedK_Notice : AephyrSharedBase
- (instancetype)initWithSeverity:(AephyrSharedK_NoticeSeverity *)severity code:(NSString *)code message:(NSString *)message __attribute__((swift_name("init(severity:code:message:)"))) __attribute__((objc_designated_initializer));
- (AephyrSharedK_Notice *)doCopySeverity:(AephyrSharedK_NoticeSeverity *)severity code:(NSString *)code message:(NSString *)message __attribute__((swift_name("doCopy(severity:code:message:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *code __attribute__((swift_name("code")));
@property (readonly) NSString *message __attribute__((swift_name("message")));
@property (readonly) AephyrSharedK_NoticeSeverity *severity __attribute__((swift_name("severity")));
@end

__attribute__((swift_name("KotlinEnum")))
@interface AephyrSharedKotlinEnum<E> : AephyrSharedBase <AephyrSharedKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_Notice.Severity")))
@interface AephyrSharedK_NoticeSeverity : AephyrSharedKotlinEnum<AephyrSharedK_NoticeSeverity *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AephyrSharedK_NoticeSeverity *info __attribute__((swift_name("info")));
@property (class, readonly) AephyrSharedK_NoticeSeverity *warning __attribute__((swift_name("warning")));
@property (class, readonly) AephyrSharedK_NoticeSeverity *alert __attribute__((swift_name("alert")));
+ (AephyrSharedKotlinArray<AephyrSharedK_NoticeSeverity *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<AephyrSharedK_NoticeSeverity *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((swift_name("K_Quantity")))
@protocol AephyrSharedK_Quantity
@required
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_QuantityMass")))
@interface AephyrSharedK_QuantityMass : AephyrSharedBase <AephyrSharedK_Quantity>
- (instancetype)initWithValue:(AephyrSharedK_Mass *)value __attribute__((swift_name("init(value:)"))) __attribute__((objc_designated_initializer));
- (AephyrSharedK_QuantityMass *)doCopyValue:(AephyrSharedK_Mass *)value __attribute__((swift_name("doCopy(value:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AephyrSharedK_Mass *value __attribute__((swift_name("value")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("ApiClient")))
@interface AephyrSharedApiClient : AephyrSharedBase
- (instancetype)initWithBaseUrl:(NSString *)baseUrl __attribute__((swift_name("init(baseUrl:)"))) __attribute__((objc_designated_initializer));

/** Callback wrapper for simple Swift interop */
- (void)healthCallback:(void (^)(NSString * _Nullable, AephyrSharedKotlinThrowable * _Nullable))callback __attribute__((swift_name("health(callback:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_Energy")))
@interface AephyrSharedK_Energy : AephyrSharedBase
- (instancetype)initWithValue:(double)value unit:(AephyrSharedK_EnergyUnit *)unit __attribute__((swift_name("init(value:unit:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedK_EnergyCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedK_Energy *)doCopyValue:(double)value unit:(AephyrSharedK_EnergyUnit *)unit __attribute__((swift_name("doCopy(value:unit:)")));
- (double)divO:(AephyrSharedK_Energy *)o __attribute__((swift_name("div(o:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (AephyrSharedK_Energy *)plusO:(AephyrSharedK_Energy *)o __attribute__((swift_name("plus(o:)")));
- (double)toTarget:(AephyrSharedK_EnergyUnit *)target __attribute__((swift_name("to(target:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AephyrSharedK_EnergyUnit *unit __attribute__((swift_name("unit")));
@property (readonly) double value __attribute__((swift_name("value")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_Energy.Companion")))
@interface AephyrSharedK_EnergyCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedK_EnergyCompanion *shared __attribute__((swift_name("shared")));
- (AephyrSharedK_Energy *)kJV:(id)v __attribute__((swift_name("kJ(v:)")));
- (AephyrSharedK_Energy *)kcalV:(id)v __attribute__((swift_name("kcal(v:)")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_EnergyUnit")))
@interface AephyrSharedK_EnergyUnit : AephyrSharedKotlinEnum<AephyrSharedK_EnergyUnit *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly, getter=companion) AephyrSharedK_EnergyUnitCompanion *companion __attribute__((swift_name("companion")));
@property (class, readonly) AephyrSharedK_EnergyUnit *kilocalorie __attribute__((swift_name("kilocalorie")));
@property (class, readonly) AephyrSharedK_EnergyUnit *kilojoule __attribute__((swift_name("kilojoule")));
+ (AephyrSharedKotlinArray<AephyrSharedK_EnergyUnit *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<AephyrSharedK_EnergyUnit *> *entries __attribute__((swift_name("entries")));
@property (readonly) double kJPerUnit __attribute__((swift_name("kJPerUnit")));
@property (readonly) NSString *symbol __attribute__((swift_name("symbol")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_EnergyUnit.Companion")))
@interface AephyrSharedK_EnergyUnitCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedK_EnergyUnitCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AephyrSharedKotlinArray<id<AephyrSharedKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_Mass")))
@interface AephyrSharedK_Mass : AephyrSharedBase
- (instancetype)initWithValue:(double)value unit:(AephyrSharedK_MassUnit *)unit __attribute__((swift_name("init(value:unit:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedK_MassCompanion *companion __attribute__((swift_name("companion")));
- (AephyrSharedK_Mass *)doCopyValue:(double)value unit:(AephyrSharedK_MassUnit *)unit __attribute__((swift_name("doCopy(value:unit:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (AephyrSharedK_Mass *)plusO:(AephyrSharedK_Mass *)o __attribute__((swift_name("plus(o:)")));
- (double)toTarget:(AephyrSharedK_MassUnit *)target __attribute__((swift_name("to(target:)")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AephyrSharedK_MassUnit *unit __attribute__((swift_name("unit")));
@property (readonly) double value __attribute__((swift_name("value")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_Mass.Companion")))
@interface AephyrSharedK_MassCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedK_MassCompanion *shared __attribute__((swift_name("shared")));
- (AephyrSharedK_Mass *)gV:(id)v __attribute__((swift_name("g(v:)")));
- (AephyrSharedK_Mass *)kgV:(id)v __attribute__((swift_name("kg(v:)")));
- (AephyrSharedK_Mass *)mgV:(id)v __attribute__((swift_name("mg(v:)")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (AephyrSharedK_Mass *)ugV:(id)v __attribute__((swift_name("ug(v:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_MassUnit")))
@interface AephyrSharedK_MassUnit : AephyrSharedKotlinEnum<AephyrSharedK_MassUnit *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly, getter=companion) AephyrSharedK_MassUnitCompanion *companion __attribute__((swift_name("companion")));
@property (class, readonly) AephyrSharedK_MassUnit *microgram __attribute__((swift_name("microgram")));
@property (class, readonly) AephyrSharedK_MassUnit *milligram __attribute__((swift_name("milligram")));
@property (class, readonly) AephyrSharedK_MassUnit *gram __attribute__((swift_name("gram")));
@property (class, readonly) AephyrSharedK_MassUnit *kilogram __attribute__((swift_name("kilogram")));
+ (AephyrSharedKotlinArray<AephyrSharedK_MassUnit *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<AephyrSharedK_MassUnit *> *entries __attribute__((swift_name("entries")));
@property (readonly) double gramsPerUnit __attribute__((swift_name("gramsPerUnit")));
@property (readonly) NSString *symbol __attribute__((swift_name("symbol")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("K_MassUnit.Companion")))
@interface AephyrSharedK_MassUnitCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedK_MassUnitCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializerTypeParamsSerializers:(AephyrSharedKotlinArray<id<AephyrSharedKotlinx_serialization_coreKSerializer>> *)typeParamsSerializers __attribute__((swift_name("serializer(typeParamsSerializers:)")));
@end

@interface AephyrSharedFoodDto (Extensions)
- (AephyrSharedFood *)toDomain __attribute__((swift_name("toDomain()")));
@end

__attribute__((swift_name("KotlinThrowable")))
@interface AephyrSharedKotlinThrowable : AephyrSharedBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(AephyrSharedKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(AephyrSharedKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.experimental.ExperimentalNativeApi
*/
- (AephyrSharedKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) AephyrSharedKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end

__attribute__((swift_name("KotlinException")))
@interface AephyrSharedKotlinException : AephyrSharedKotlinThrowable
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(AephyrSharedKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(AephyrSharedKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinRuntimeException")))
@interface AephyrSharedKotlinRuntimeException : AephyrSharedKotlinException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(AephyrSharedKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(AephyrSharedKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("KotlinIllegalStateException")))
@interface AephyrSharedKotlinIllegalStateException : AephyrSharedKotlinRuntimeException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(AephyrSharedKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(AephyrSharedKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.4")
*/
__attribute__((swift_name("KotlinCancellationException")))
@interface AephyrSharedKotlinCancellationException : AephyrSharedKotlinIllegalStateException
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(AephyrSharedKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(AephyrSharedKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreFlow")))
@protocol AephyrSharedKotlinx_coroutines_coreFlow
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)collectCollector:(id<AephyrSharedKotlinx_coroutines_coreFlowCollector>)collector completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("collect(collector:completionHandler:)")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreSharedFlow")))
@protocol AephyrSharedKotlinx_coroutines_coreSharedFlow <AephyrSharedKotlinx_coroutines_coreFlow>
@required
@property (readonly) NSArray<id> *replayCache __attribute__((swift_name("replayCache")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreStateFlow")))
@protocol AephyrSharedKotlinx_coroutines_coreStateFlow <AephyrSharedKotlinx_coroutines_coreSharedFlow>
@required
@property (readonly) id _Nullable value __attribute__((swift_name("value")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializationStrategy")))
@protocol AephyrSharedKotlinx_serialization_coreSerializationStrategy
@required
- (void)serializeEncoder:(id<AephyrSharedKotlinx_serialization_coreEncoder>)encoder value:(id _Nullable)value __attribute__((swift_name("serialize(encoder:value:)")));
@property (readonly) id<AephyrSharedKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDeserializationStrategy")))
@protocol AephyrSharedKotlinx_serialization_coreDeserializationStrategy
@required
- (id _Nullable)deserializeDecoder:(id<AephyrSharedKotlinx_serialization_coreDecoder>)decoder __attribute__((swift_name("deserialize(decoder:)")));
@property (readonly) id<AephyrSharedKotlinx_serialization_coreSerialDescriptor> descriptor __attribute__((swift_name("descriptor")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreKSerializer")))
@protocol AephyrSharedKotlinx_serialization_coreKSerializer <AephyrSharedKotlinx_serialization_coreSerializationStrategy, AephyrSharedKotlinx_serialization_coreDeserializationStrategy>
@required
@end

__attribute__((swift_name("Kotlinx_coroutines_coreCoroutineScope")))
@protocol AephyrSharedKotlinx_coroutines_coreCoroutineScope
@required
@property (readonly) id<AephyrSharedKotlinCoroutineContext> coroutineContext __attribute__((swift_name("coroutineContext")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/LocalDateIso8601Serializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDate")))
@interface AephyrSharedKotlinx_datetimeLocalDate : AephyrSharedBase <AephyrSharedKotlinComparable>
- (instancetype)initWithYear:(int32_t)year monthNumber:(int32_t)monthNumber dayOfMonth:(int32_t)dayOfMonth __attribute__((swift_name("init(year:monthNumber:dayOfMonth:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithYear:(int32_t)year month:(AephyrSharedKotlinx_datetimeMonth *)month dayOfMonth:(int32_t)dayOfMonth __attribute__((swift_name("init(year:month:dayOfMonth:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedKotlinx_datetimeLocalDateCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(AephyrSharedKotlinx_datetimeLocalDate *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (int32_t)toEpochDays __attribute__((swift_name("toEpochDays()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int32_t dayOfMonth __attribute__((swift_name("dayOfMonth")));
@property (readonly) AephyrSharedKotlinx_datetimeDayOfWeek *dayOfWeek __attribute__((swift_name("dayOfWeek")));
@property (readonly) int32_t dayOfYear __attribute__((swift_name("dayOfYear")));
@property (readonly) AephyrSharedKotlinx_datetimeMonth *month __attribute__((swift_name("month")));
@property (readonly) int32_t monthNumber __attribute__((swift_name("monthNumber")));
@property (readonly) int32_t year __attribute__((swift_name("year")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.Serializable(with=NormalClass(value=kotlinx/datetime/serializers/InstantIso8601Serializer))
*/
__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeInstant")))
@interface AephyrSharedKotlinx_datetimeInstant : AephyrSharedBase <AephyrSharedKotlinComparable>
@property (class, readonly, getter=companion) AephyrSharedKotlinx_datetimeInstantCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(AephyrSharedKotlinx_datetimeInstant *)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (AephyrSharedKotlinx_datetimeInstant *)minusDuration:(int64_t)duration __attribute__((swift_name("minus(duration:)")));
- (int64_t)minusOther:(AephyrSharedKotlinx_datetimeInstant *)other __attribute__((swift_name("minus(other:)")));
- (AephyrSharedKotlinx_datetimeInstant *)plusDuration:(int64_t)duration __attribute__((swift_name("plus(duration:)")));
- (int64_t)toEpochMilliseconds __attribute__((swift_name("toEpochMilliseconds()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) int64_t epochSeconds __attribute__((swift_name("epochSeconds")));
@property (readonly) int32_t nanosecondsOfSecond __attribute__((swift_name("nanosecondsOfSecond")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface AephyrSharedKotlinEnumCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface AephyrSharedKotlinArray<T> : AephyrSharedBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(AephyrSharedInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<AephyrSharedKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("Kotlinx_coroutines_coreFlowCollector")))
@protocol AephyrSharedKotlinx_coroutines_coreFlowCollector
@required

/**
 * @note This method converts instances of CancellationException to errors.
 * Other uncaught Kotlin exceptions are fatal.
*/
- (void)emitValue:(id _Nullable)value completionHandler:(void (^)(NSError * _Nullable))completionHandler __attribute__((swift_name("emit(value:completionHandler:)")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreEncoder")))
@protocol AephyrSharedKotlinx_serialization_coreEncoder
@required
- (id<AephyrSharedKotlinx_serialization_coreCompositeEncoder>)beginCollectionDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor collectionSize:(int32_t)collectionSize __attribute__((swift_name("beginCollection(descriptor:collectionSize:)")));
- (id<AephyrSharedKotlinx_serialization_coreCompositeEncoder>)beginStructureDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (void)encodeBooleanValue:(BOOL)value __attribute__((swift_name("encodeBoolean(value:)")));
- (void)encodeByteValue:(int8_t)value __attribute__((swift_name("encodeByte(value:)")));
- (void)encodeCharValue:(unichar)value __attribute__((swift_name("encodeChar(value:)")));
- (void)encodeDoubleValue:(double)value __attribute__((swift_name("encodeDouble(value:)")));
- (void)encodeEnumEnumDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)enumDescriptor index:(int32_t)index __attribute__((swift_name("encodeEnum(enumDescriptor:index:)")));
- (void)encodeFloatValue:(float)value __attribute__((swift_name("encodeFloat(value:)")));
- (id<AephyrSharedKotlinx_serialization_coreEncoder>)encodeInlineDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("encodeInline(descriptor:)")));
- (void)encodeIntValue:(int32_t)value __attribute__((swift_name("encodeInt(value:)")));
- (void)encodeLongValue:(int64_t)value __attribute__((swift_name("encodeLong(value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNotNullMark __attribute__((swift_name("encodeNotNullMark()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNull __attribute__((swift_name("encodeNull()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNullableSerializableValueSerializer:(id<AephyrSharedKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableValue(serializer:value:)")));
- (void)encodeSerializableValueSerializer:(id<AephyrSharedKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableValue(serializer:value:)")));
- (void)encodeShortValue:(int16_t)value __attribute__((swift_name("encodeShort(value:)")));
- (void)encodeStringValue:(NSString *)value __attribute__((swift_name("encodeString(value:)")));
@property (readonly) AephyrSharedKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerialDescriptor")))
@protocol AephyrSharedKotlinx_serialization_coreSerialDescriptor
@required

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (NSArray<id<AephyrSharedKotlinAnnotation>> *)getElementAnnotationsIndex:(int32_t)index __attribute__((swift_name("getElementAnnotations(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)getElementDescriptorIndex:(int32_t)index __attribute__((swift_name("getElementDescriptor(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (int32_t)getElementIndexName:(NSString *)name __attribute__((swift_name("getElementIndex(name:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (NSString *)getElementNameIndex:(int32_t)index __attribute__((swift_name("getElementName(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)isElementOptionalIndex:(int32_t)index __attribute__((swift_name("isElementOptional(index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) NSArray<id<AephyrSharedKotlinAnnotation>> *annotations __attribute__((swift_name("annotations")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) int32_t elementsCount __attribute__((swift_name("elementsCount")));
@property (readonly) BOOL isInline __attribute__((swift_name("isInline")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) BOOL isNullable __attribute__((swift_name("isNullable")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) AephyrSharedKotlinx_serialization_coreSerialKind *kind __attribute__((swift_name("kind")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
@property (readonly) NSString *serialName __attribute__((swift_name("serialName")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreDecoder")))
@protocol AephyrSharedKotlinx_serialization_coreDecoder
@required
- (id<AephyrSharedKotlinx_serialization_coreCompositeDecoder>)beginStructureDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("beginStructure(descriptor:)")));
- (BOOL)decodeBoolean __attribute__((swift_name("decodeBoolean()")));
- (int8_t)decodeByte __attribute__((swift_name("decodeByte()")));
- (unichar)decodeChar __attribute__((swift_name("decodeChar()")));
- (double)decodeDouble __attribute__((swift_name("decodeDouble()")));
- (int32_t)decodeEnumEnumDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)enumDescriptor __attribute__((swift_name("decodeEnum(enumDescriptor:)")));
- (float)decodeFloat __attribute__((swift_name("decodeFloat()")));
- (id<AephyrSharedKotlinx_serialization_coreDecoder>)decodeInlineDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeInline(descriptor:)")));
- (int32_t)decodeInt __attribute__((swift_name("decodeInt()")));
- (int64_t)decodeLong __attribute__((swift_name("decodeLong()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)decodeNotNullMark __attribute__((swift_name("decodeNotNullMark()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (AephyrSharedKotlinNothing * _Nullable)decodeNull __attribute__((swift_name("decodeNull()")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableValueDeserializer:(id<AephyrSharedKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeNullableSerializableValue(deserializer:)")));
- (id _Nullable)decodeSerializableValueDeserializer:(id<AephyrSharedKotlinx_serialization_coreDeserializationStrategy>)deserializer __attribute__((swift_name("decodeSerializableValue(deserializer:)")));
- (int16_t)decodeShort __attribute__((swift_name("decodeShort()")));
- (NSString *)decodeString __attribute__((swift_name("decodeString()")));
@property (readonly) AephyrSharedKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.3")
*/
__attribute__((swift_name("KotlinCoroutineContext")))
@protocol AephyrSharedKotlinCoroutineContext
@required
- (id _Nullable)foldInitial:(id _Nullable)initial operation:(id _Nullable (^)(id _Nullable, id<AephyrSharedKotlinCoroutineContextElement>))operation __attribute__((swift_name("fold(initial:operation:)")));
- (id<AephyrSharedKotlinCoroutineContextElement> _Nullable)getKey:(id<AephyrSharedKotlinCoroutineContextKey>)key __attribute__((swift_name("get(key:)")));
- (id<AephyrSharedKotlinCoroutineContext>)minusKeyKey:(id<AephyrSharedKotlinCoroutineContextKey>)key __attribute__((swift_name("minusKey(key:)")));
- (id<AephyrSharedKotlinCoroutineContext>)plusContext:(id<AephyrSharedKotlinCoroutineContext>)context __attribute__((swift_name("plus(context:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeMonth")))
@interface AephyrSharedKotlinx_datetimeMonth : AephyrSharedKotlinEnum<AephyrSharedKotlinx_datetimeMonth *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *january __attribute__((swift_name("january")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *february __attribute__((swift_name("february")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *march __attribute__((swift_name("march")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *april __attribute__((swift_name("april")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *may __attribute__((swift_name("may")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *june __attribute__((swift_name("june")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *july __attribute__((swift_name("july")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *august __attribute__((swift_name("august")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *september __attribute__((swift_name("september")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *october __attribute__((swift_name("october")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *november __attribute__((swift_name("november")));
@property (class, readonly) AephyrSharedKotlinx_datetimeMonth *december __attribute__((swift_name("december")));
+ (AephyrSharedKotlinArray<AephyrSharedKotlinx_datetimeMonth *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<AephyrSharedKotlinx_datetimeMonth *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeLocalDate.Companion")))
@interface AephyrSharedKotlinx_datetimeLocalDateCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedKotlinx_datetimeLocalDateCompanion *shared __attribute__((swift_name("shared")));
- (id<AephyrSharedKotlinx_datetimeDateTimeFormat>)FormatBlock:(void (^)(id<AephyrSharedKotlinx_datetimeDateTimeFormatBuilderWithDate>))block __attribute__((swift_name("Format(block:)")));
- (AephyrSharedKotlinx_datetimeLocalDate *)fromEpochDaysEpochDays:(int32_t)epochDays __attribute__((swift_name("fromEpochDays(epochDays:)")));
- (AephyrSharedKotlinx_datetimeLocalDate *)parseInput:(id)input format:(id<AephyrSharedKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDayOfWeek")))
@interface AephyrSharedKotlinx_datetimeDayOfWeek : AephyrSharedKotlinEnum<AephyrSharedKotlinx_datetimeDayOfWeek *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AephyrSharedKotlinx_datetimeDayOfWeek *monday __attribute__((swift_name("monday")));
@property (class, readonly) AephyrSharedKotlinx_datetimeDayOfWeek *tuesday __attribute__((swift_name("tuesday")));
@property (class, readonly) AephyrSharedKotlinx_datetimeDayOfWeek *wednesday __attribute__((swift_name("wednesday")));
@property (class, readonly) AephyrSharedKotlinx_datetimeDayOfWeek *thursday __attribute__((swift_name("thursday")));
@property (class, readonly) AephyrSharedKotlinx_datetimeDayOfWeek *friday __attribute__((swift_name("friday")));
@property (class, readonly) AephyrSharedKotlinx_datetimeDayOfWeek *saturday __attribute__((swift_name("saturday")));
@property (class, readonly) AephyrSharedKotlinx_datetimeDayOfWeek *sunday __attribute__((swift_name("sunday")));
+ (AephyrSharedKotlinArray<AephyrSharedKotlinx_datetimeDayOfWeek *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<AephyrSharedKotlinx_datetimeDayOfWeek *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeInstant.Companion")))
@interface AephyrSharedKotlinx_datetimeInstantCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedKotlinx_datetimeInstantCompanion *shared __attribute__((swift_name("shared")));
- (AephyrSharedKotlinx_datetimeInstant *)fromEpochMillisecondsEpochMilliseconds:(int64_t)epochMilliseconds __attribute__((swift_name("fromEpochMilliseconds(epochMilliseconds:)")));
- (AephyrSharedKotlinx_datetimeInstant *)fromEpochSecondsEpochSeconds:(int64_t)epochSeconds nanosecondAdjustment:(int32_t)nanosecondAdjustment __attribute__((swift_name("fromEpochSeconds(epochSeconds:nanosecondAdjustment:)")));
- (AephyrSharedKotlinx_datetimeInstant *)fromEpochSecondsEpochSeconds:(int64_t)epochSeconds nanosecondAdjustment_:(int64_t)nanosecondAdjustment __attribute__((swift_name("fromEpochSeconds(epochSeconds:nanosecondAdjustment_:)")));
- (AephyrSharedKotlinx_datetimeInstant *)now __attribute__((swift_name("now()"))) __attribute__((unavailable("Use Clock.System.now() instead")));
- (AephyrSharedKotlinx_datetimeInstant *)parseInput:(id)input format:(id<AephyrSharedKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("parse(input:format:)")));
- (id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("serializer()")));
@property (readonly) AephyrSharedKotlinx_datetimeInstant *DISTANT_FUTURE __attribute__((swift_name("DISTANT_FUTURE")));
@property (readonly) AephyrSharedKotlinx_datetimeInstant *DISTANT_PAST __attribute__((swift_name("DISTANT_PAST")));
@end

__attribute__((swift_name("KotlinIterator")))
@protocol AephyrSharedKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeEncoder")))
@protocol AephyrSharedKotlinx_serialization_coreCompositeEncoder
@required
- (void)encodeBooleanElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(BOOL)value __attribute__((swift_name("encodeBooleanElement(descriptor:index:value:)")));
- (void)encodeByteElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int8_t)value __attribute__((swift_name("encodeByteElement(descriptor:index:value:)")));
- (void)encodeCharElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(unichar)value __attribute__((swift_name("encodeCharElement(descriptor:index:value:)")));
- (void)encodeDoubleElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(double)value __attribute__((swift_name("encodeDoubleElement(descriptor:index:value:)")));
- (void)encodeFloatElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(float)value __attribute__((swift_name("encodeFloatElement(descriptor:index:value:)")));
- (id<AephyrSharedKotlinx_serialization_coreEncoder>)encodeInlineElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("encodeInlineElement(descriptor:index:)")));
- (void)encodeIntElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int32_t)value __attribute__((swift_name("encodeIntElement(descriptor:index:value:)")));
- (void)encodeLongElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int64_t)value __attribute__((swift_name("encodeLongElement(descriptor:index:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)encodeNullableSerializableElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<AephyrSharedKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeNullableSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeSerializableElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index serializer:(id<AephyrSharedKotlinx_serialization_coreSerializationStrategy>)serializer value:(id _Nullable)value __attribute__((swift_name("encodeSerializableElement(descriptor:index:serializer:value:)")));
- (void)encodeShortElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(int16_t)value __attribute__((swift_name("encodeShortElement(descriptor:index:value:)")));
- (void)encodeStringElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index value:(NSString *)value __attribute__((swift_name("encodeStringElement(descriptor:index:value:)")));
- (void)endStructureDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)shouldEncodeElementDefaultDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("shouldEncodeElementDefault(descriptor:index:)")));
@property (readonly) AephyrSharedKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreSerializersModule")))
@interface AephyrSharedKotlinx_serialization_coreSerializersModule : AephyrSharedBase

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (void)dumpToCollector:(id<AephyrSharedKotlinx_serialization_coreSerializersModuleCollector>)collector __attribute__((swift_name("dumpTo(collector:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<AephyrSharedKotlinx_serialization_coreKSerializer> _Nullable)getContextualKClass:(id<AephyrSharedKotlinKClass>)kClass typeArgumentsSerializers:(NSArray<id<AephyrSharedKotlinx_serialization_coreKSerializer>> *)typeArgumentsSerializers __attribute__((swift_name("getContextual(kClass:typeArgumentsSerializers:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<AephyrSharedKotlinx_serialization_coreSerializationStrategy> _Nullable)getPolymorphicBaseClass:(id<AephyrSharedKotlinKClass>)baseClass value:(id)value __attribute__((swift_name("getPolymorphic(baseClass:value:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id<AephyrSharedKotlinx_serialization_coreDeserializationStrategy> _Nullable)getPolymorphicBaseClass:(id<AephyrSharedKotlinKClass>)baseClass serializedClassName:(NSString * _Nullable)serializedClassName __attribute__((swift_name("getPolymorphic(baseClass:serializedClassName:)")));
@end

__attribute__((swift_name("KotlinAnnotation")))
@protocol AephyrSharedKotlinAnnotation
@required
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerialKind")))
@interface AephyrSharedKotlinx_serialization_coreSerialKind : AephyrSharedBase
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@end

__attribute__((swift_name("Kotlinx_serialization_coreCompositeDecoder")))
@protocol AephyrSharedKotlinx_serialization_coreCompositeDecoder
@required
- (BOOL)decodeBooleanElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeBooleanElement(descriptor:index:)")));
- (int8_t)decodeByteElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeByteElement(descriptor:index:)")));
- (unichar)decodeCharElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeCharElement(descriptor:index:)")));
- (int32_t)decodeCollectionSizeDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeCollectionSize(descriptor:)")));
- (double)decodeDoubleElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeDoubleElement(descriptor:index:)")));
- (int32_t)decodeElementIndexDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("decodeElementIndex(descriptor:)")));
- (float)decodeFloatElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeFloatElement(descriptor:index:)")));
- (id<AephyrSharedKotlinx_serialization_coreDecoder>)decodeInlineElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeInlineElement(descriptor:index:)")));
- (int32_t)decodeIntElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeIntElement(descriptor:index:)")));
- (int64_t)decodeLongElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeLongElement(descriptor:index:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (id _Nullable)decodeNullableSerializableElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<AephyrSharedKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeNullableSerializableElement(descriptor:index:deserializer:previousValue:)")));

/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
- (BOOL)decodeSequentially __attribute__((swift_name("decodeSequentially()")));
- (id _Nullable)decodeSerializableElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index deserializer:(id<AephyrSharedKotlinx_serialization_coreDeserializationStrategy>)deserializer previousValue:(id _Nullable)previousValue __attribute__((swift_name("decodeSerializableElement(descriptor:index:deserializer:previousValue:)")));
- (int16_t)decodeShortElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeShortElement(descriptor:index:)")));
- (NSString *)decodeStringElementDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor index:(int32_t)index __attribute__((swift_name("decodeStringElement(descriptor:index:)")));
- (void)endStructureDescriptor:(id<AephyrSharedKotlinx_serialization_coreSerialDescriptor>)descriptor __attribute__((swift_name("endStructure(descriptor:)")));
@property (readonly) AephyrSharedKotlinx_serialization_coreSerializersModule *serializersModule __attribute__((swift_name("serializersModule")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinNothing")))
@interface AephyrSharedKotlinNothing : AephyrSharedBase
@end

__attribute__((swift_name("KotlinCoroutineContextElement")))
@protocol AephyrSharedKotlinCoroutineContextElement <AephyrSharedKotlinCoroutineContext>
@required
@property (readonly) id<AephyrSharedKotlinCoroutineContextKey> key __attribute__((swift_name("key")));
@end

__attribute__((swift_name("KotlinCoroutineContextKey")))
@protocol AephyrSharedKotlinCoroutineContextKey
@required
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormat")))
@protocol AephyrSharedKotlinx_datetimeDateTimeFormat
@required
- (NSString *)formatValue:(id _Nullable)value __attribute__((swift_name("format(value:)")));
- (id<AephyrSharedKotlinAppendable>)formatToAppendable:(id<AephyrSharedKotlinAppendable>)appendable value:(id _Nullable)value __attribute__((swift_name("formatTo(appendable:value:)")));
- (id _Nullable)parseInput:(id)input __attribute__((swift_name("parse(input:)")));
- (id _Nullable)parseOrNullInput:(id)input __attribute__((swift_name("parseOrNull(input:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilder")))
@protocol AephyrSharedKotlinx_datetimeDateTimeFormatBuilder
@required
- (void)charsValue:(NSString *)value __attribute__((swift_name("chars(value:)")));
@end

__attribute__((swift_name("Kotlinx_datetimeDateTimeFormatBuilderWithDate")))
@protocol AephyrSharedKotlinx_datetimeDateTimeFormatBuilderWithDate <AephyrSharedKotlinx_datetimeDateTimeFormatBuilder>
@required
- (void)dateFormat:(id<AephyrSharedKotlinx_datetimeDateTimeFormat>)format __attribute__((swift_name("date(format:)")));
- (void)dayOfMonthPadding:(AephyrSharedKotlinx_datetimePadding *)padding __attribute__((swift_name("dayOfMonth(padding:)")));
- (void)dayOfWeekNames:(AephyrSharedKotlinx_datetimeDayOfWeekNames *)names __attribute__((swift_name("dayOfWeek(names:)")));
- (void)monthNameNames:(AephyrSharedKotlinx_datetimeMonthNames *)names __attribute__((swift_name("monthName(names:)")));
- (void)monthNumberPadding:(AephyrSharedKotlinx_datetimePadding *)padding __attribute__((swift_name("monthNumber(padding:)")));
- (void)yearPadding:(AephyrSharedKotlinx_datetimePadding *)padding __attribute__((swift_name("year(padding:)")));
- (void)yearTwoDigitsBaseYear:(int32_t)baseYear __attribute__((swift_name("yearTwoDigits(baseYear:)")));
@end


/**
 * @note annotations
 *   kotlinx.serialization.ExperimentalSerializationApi
*/
__attribute__((swift_name("Kotlinx_serialization_coreSerializersModuleCollector")))
@protocol AephyrSharedKotlinx_serialization_coreSerializersModuleCollector
@required
- (void)contextualKClass:(id<AephyrSharedKotlinKClass>)kClass provider:(id<AephyrSharedKotlinx_serialization_coreKSerializer> (^)(NSArray<id<AephyrSharedKotlinx_serialization_coreKSerializer>> *))provider __attribute__((swift_name("contextual(kClass:provider:)")));
- (void)contextualKClass:(id<AephyrSharedKotlinKClass>)kClass serializer:(id<AephyrSharedKotlinx_serialization_coreKSerializer>)serializer __attribute__((swift_name("contextual(kClass:serializer:)")));
- (void)polymorphicBaseClass:(id<AephyrSharedKotlinKClass>)baseClass actualClass:(id<AephyrSharedKotlinKClass>)actualClass actualSerializer:(id<AephyrSharedKotlinx_serialization_coreKSerializer>)actualSerializer __attribute__((swift_name("polymorphic(baseClass:actualClass:actualSerializer:)")));
- (void)polymorphicDefaultBaseClass:(id<AephyrSharedKotlinKClass>)baseClass defaultDeserializerProvider:(id<AephyrSharedKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefault(baseClass:defaultDeserializerProvider:)"))) __attribute__((deprecated("Deprecated in favor of function with more precise name: polymorphicDefaultDeserializer")));
- (void)polymorphicDefaultDeserializerBaseClass:(id<AephyrSharedKotlinKClass>)baseClass defaultDeserializerProvider:(id<AephyrSharedKotlinx_serialization_coreDeserializationStrategy> _Nullable (^)(NSString * _Nullable))defaultDeserializerProvider __attribute__((swift_name("polymorphicDefaultDeserializer(baseClass:defaultDeserializerProvider:)")));
- (void)polymorphicDefaultSerializerBaseClass:(id<AephyrSharedKotlinKClass>)baseClass defaultSerializerProvider:(id<AephyrSharedKotlinx_serialization_coreSerializationStrategy> _Nullable (^)(id))defaultSerializerProvider __attribute__((swift_name("polymorphicDefaultSerializer(baseClass:defaultSerializerProvider:)")));
@end

__attribute__((swift_name("KotlinKDeclarationContainer")))
@protocol AephyrSharedKotlinKDeclarationContainer
@required
@end

__attribute__((swift_name("KotlinKAnnotatedElement")))
@protocol AephyrSharedKotlinKAnnotatedElement
@required
@end


/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
__attribute__((swift_name("KotlinKClassifier")))
@protocol AephyrSharedKotlinKClassifier
@required
@end

__attribute__((swift_name("KotlinKClass")))
@protocol AephyrSharedKotlinKClass <AephyrSharedKotlinKDeclarationContainer, AephyrSharedKotlinKAnnotatedElement, AephyrSharedKotlinKClassifier>
@required

/**
 * @note annotations
 *   kotlin.SinceKotlin(version="1.1")
*/
- (BOOL)isInstanceValue:(id _Nullable)value __attribute__((swift_name("isInstance(value:)")));
@property (readonly) NSString * _Nullable qualifiedName __attribute__((swift_name("qualifiedName")));
@property (readonly) NSString * _Nullable simpleName __attribute__((swift_name("simpleName")));
@end

__attribute__((swift_name("KotlinAppendable")))
@protocol AephyrSharedKotlinAppendable
@required
- (id<AephyrSharedKotlinAppendable>)appendValue:(unichar)value __attribute__((swift_name("append(value:)")));
- (id<AephyrSharedKotlinAppendable>)appendValue_:(id _Nullable)value __attribute__((swift_name("append(value_:)")));
- (id<AephyrSharedKotlinAppendable>)appendValue:(id _Nullable)value startIndex:(int32_t)startIndex endIndex:(int32_t)endIndex __attribute__((swift_name("append(value:startIndex:endIndex:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimePadding")))
@interface AephyrSharedKotlinx_datetimePadding : AephyrSharedKotlinEnum<AephyrSharedKotlinx_datetimePadding *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) AephyrSharedKotlinx_datetimePadding *none __attribute__((swift_name("none")));
@property (class, readonly) AephyrSharedKotlinx_datetimePadding *zero __attribute__((swift_name("zero")));
@property (class, readonly) AephyrSharedKotlinx_datetimePadding *space __attribute__((swift_name("space")));
+ (AephyrSharedKotlinArray<AephyrSharedKotlinx_datetimePadding *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<AephyrSharedKotlinx_datetimePadding *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDayOfWeekNames")))
@interface AephyrSharedKotlinx_datetimeDayOfWeekNames : AephyrSharedBase
- (instancetype)initWithNames:(NSArray<NSString *> *)names __attribute__((swift_name("init(names:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMonday:(NSString *)monday tuesday:(NSString *)tuesday wednesday:(NSString *)wednesday thursday:(NSString *)thursday friday:(NSString *)friday saturday:(NSString *)saturday sunday:(NSString *)sunday __attribute__((swift_name("init(monday:tuesday:wednesday:thursday:friday:saturday:sunday:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedKotlinx_datetimeDayOfWeekNamesCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<NSString *> *names __attribute__((swift_name("names")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeMonthNames")))
@interface AephyrSharedKotlinx_datetimeMonthNames : AephyrSharedBase
- (instancetype)initWithNames:(NSArray<NSString *> *)names __attribute__((swift_name("init(names:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithJanuary:(NSString *)january february:(NSString *)february march:(NSString *)march april:(NSString *)april may:(NSString *)may june:(NSString *)june july:(NSString *)july august:(NSString *)august september:(NSString *)september october:(NSString *)october november:(NSString *)november december:(NSString *)december __attribute__((swift_name("init(january:february:march:april:may:june:july:august:september:october:november:december:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) AephyrSharedKotlinx_datetimeMonthNamesCompanion *companion __attribute__((swift_name("companion")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSArray<NSString *> *names __attribute__((swift_name("names")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeDayOfWeekNames.Companion")))
@interface AephyrSharedKotlinx_datetimeDayOfWeekNamesCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedKotlinx_datetimeDayOfWeekNamesCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) AephyrSharedKotlinx_datetimeDayOfWeekNames *ENGLISH_ABBREVIATED __attribute__((swift_name("ENGLISH_ABBREVIATED")));
@property (readonly) AephyrSharedKotlinx_datetimeDayOfWeekNames *ENGLISH_FULL __attribute__((swift_name("ENGLISH_FULL")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("Kotlinx_datetimeMonthNames.Companion")))
@interface AephyrSharedKotlinx_datetimeMonthNamesCompanion : AephyrSharedBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) AephyrSharedKotlinx_datetimeMonthNamesCompanion *shared __attribute__((swift_name("shared")));
@property (readonly) AephyrSharedKotlinx_datetimeMonthNames *ENGLISH_ABBREVIATED __attribute__((swift_name("ENGLISH_ABBREVIATED")));
@property (readonly) AephyrSharedKotlinx_datetimeMonthNames *ENGLISH_FULL __attribute__((swift_name("ENGLISH_FULL")));
@end

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
