using Automation.Model.enums;

namespace Automation.Dto.Request;

public record PriceThresholdReqDto(
    double ThresholdPrice,
    TransactionType TransactionType,
    ThresholdType ThresholdType,
    string Username);