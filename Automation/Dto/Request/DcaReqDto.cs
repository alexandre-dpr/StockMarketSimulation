using Automation.Model.enums;

namespace Automation.Dto.Request;

public record DcaReqDto(string Username, string Symbole, int Quantite, Frequency Frequence);