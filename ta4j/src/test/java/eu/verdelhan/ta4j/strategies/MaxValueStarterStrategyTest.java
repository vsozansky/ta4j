/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Marc de Verdelhan & respective authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.verdelhan.ta4j.strategies;

import eu.verdelhan.ta4j.Operation;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.mocks.MockIndicator;
import eu.verdelhan.ta4j.mocks.MockStrategy;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaxValueStarterStrategyTest {

    private MockIndicator<Double> indicator;

    private int startValue;

    private Operation[] enter;

    private Operation[] exit;

    private Strategy alwaysBuy;

    private Strategy starter;

    @Before
    public void setUp() {
        indicator = new MockIndicator<Double>(96d, 95d, 90d, 92d, 95d);
        startValue = 93;
        enter = new Operation[] { Operation.buyAt(0), Operation.buyAt(1), 
                Operation.buyAt(2), Operation.buyAt(3), Operation.buyAt(4) };
        exit = new Operation[] { null, null, null, null, null };
        alwaysBuy = new MockStrategy(enter, exit);
        starter = new MaxValueStarterStrategy(indicator, alwaysBuy, startValue);
    }

    @Test
    public void strategyShouldBuy() {
        Trade trade = new Trade();

        Operation buy = Operation.buyAt(2);
        assertTrue(starter.shouldOperate(trade, 2));
        trade.operate(2);
        assertEquals(buy, trade.getEntry());

        trade = new Trade();
        buy = Operation.buyAt(3);

        assertTrue(starter.shouldOperate(trade, 3));
        trade.operate(3);

        assertFalse(starter.shouldOperate(trade, 3));
        assertEquals(buy, trade.getEntry());
    }

    @Test
    public void strategyShouldNotBuyEvenIfFakeIsSayingTo() {
        Trade trade = new Trade();
        assertFalse(starter.shouldOperate(trade, 0));
        assertFalse(starter.shouldOperate(trade, 1));
        assertFalse(starter.shouldOperate(trade, 4));
    }
}
