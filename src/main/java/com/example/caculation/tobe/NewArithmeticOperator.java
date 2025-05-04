package com.example.caculation.tobe;

import com.example.caculation.domain.PositiveNumber;

public interface NewArithmeticOperator {

  boolean supports(String operator);

  int calculate(PositiveNumber operand1, PositiveNumber operand2);

}
