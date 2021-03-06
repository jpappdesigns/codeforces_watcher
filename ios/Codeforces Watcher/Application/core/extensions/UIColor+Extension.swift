//
// Created by Yevhenii Kanivets on 11/01/2020.
// Copyright (c) 2020 xorum.io. All rights reserved.
//

import Foundation
import UIKit

extension UIColor {

    convenience init(red: Int, green: Int, blue: Int) {
        assert(red >= 0 && red <= 255, "Invalid red component")
        assert(green >= 0 && green <= 255, "Invalid green component")
        assert(blue >= 0 && blue <= 255, "Invalid blue component")

        self.init(red: CGFloat(red) / 255.0, green: CGFloat(green) / 255.0, blue: CGFloat(blue) / 255.0, alpha: 1.0)
    }

    convenience init(rgb: Int) {
        self.init(
            red: (rgb >> 16) & 0xFF,
            green: (rgb >> 8) & 0xFF,
            blue: rgb & 0xFF
        )
    }
}

func colorTextByUserRank(text: String, rank: String?) -> NSMutableAttributedString {
    var color = UIColor()

    switch (rank) {
    case nil:
        color = Palette.black
    case "newbie":
        color = Palette.grey
    case "pupil":
        color = Palette.green
    case "specialist":
        color = Palette.blueGreen
    case "expert":
        color = Palette.blue
    case "candidate master":
        color = Palette.purple
    case "master":
        color = Palette.orange
    case "international master":
        color = Palette.orange
    case "grandmaster":
        color = Palette.red
    case "international grandmaster", "legendary grandmaster":
        color = Palette.red
    default:
        color = Palette.grey
    }

    let attributedText = NSMutableAttributedString(string: text).apply {
        $0.addAttribute(NSAttributedString.Key.foregroundColor, value: color, range: NSRange(location: 0, length: text.count))
    }

    if rank == "legendary grandmaster" {
        attributedText.addAttribute(NSAttributedString.Key.foregroundColor, value: Palette.black, range: NSRange(location: 0, length: 1))
    }

    return attributedText
}
