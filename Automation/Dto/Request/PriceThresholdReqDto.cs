using Automation.Model.enums;

namespace Automation.Dto.Request;

public record PriceThresholdReqDto(
    string Ticker,
    double ThresholdPrice,
    TransactionType TransactionType,
    ThresholdType ThresholdType
);