using Automation.Model.enums;

namespace Automation.Dto.Request;

public record DcaReqDto(string Symbole, int Quantite, Frequency Frequence, TransactionType TransactionType);