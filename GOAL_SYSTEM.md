# Custom Goal System - User Guide

## 🎯 Overview

The Parva app now supports **custom goals** at the Parva level, allowing you to overlay your specific objectives on top of the general theme structure.

---

## 📊 Goal Hierarchy

```
Maha-Parva (343 days)
├─ Title: "Be awesome in React" ← Your main objective
├─ Description: "Master React..." ← Overall description
└─ 7 Parvas (49 days each)
    ├─ Parva 1: Beginning (Violet)
    │   ├─ Theme: "Foundation is laid" ← Automatic theme
    │   └─ Custom Goal: "Learn React Hooks" ← YOU set this!
    ├─ Parva 2: Practice (Indigo)
    │   └─ Custom Goal: "Build 3 projects with Hooks"
    ├─ Parva 3: Discernment (Blue)
    │   └─ Custom Goal: "Understand component patterns"
    └─ ... and so on
```

---

## ✨ Features

### 1. **Custom Goal Field** for Each Parva

Each 49-day Parva has:
- **Automatic theme** (Beginning/Practice/etc.) - Set by the system
- **Custom goal** - Set by YOU

Example:
```
Parva 1: Beginning
Theme: "The foundation is laid"
Your Goal: "Learn React Hooks and useState"
```

### 2. **Flexible Planning**

- ✅ **Edit future Parvas** - Plan ahead as much as you want
- ✅ **Edit current Parva** - Adjust as you go
- ❌ **Cannot edit past Parvas** - History is locked to preserve your journey

### 3. **Smart Goal Templates**

Each theme comes with **5 goal prompts** tailored to that phase:

#### Beginning (Violet) - Foundation
- "Learn the fundamentals of..."
- "Establish a foundation in..."
- "Start exploring..."
- "Build basic knowledge of..."
- "Get introduced to..."

#### Practice (Indigo) - Repetition
- "Practice daily with..."
- "Build consistency in..."
- "Repeat and reinforce..."
- "Develop muscle memory for..."
- "Do exercises on..."

#### Discernment (Blue) - Clarity
- "Analyze and evaluate..."
- "Identify patterns in..."
- "Understand what works in..."
- "Refine my approach to..."
- "Debug and troubleshoot..."

#### Ascent (Green) - Growth
- "Push beyond basics in..."
- "Take on challenging..."
- "Accelerate growth in..."
- "Build advanced skills in..."
- "Go deeper into..."

#### Mastery (Yellow) - Expertise
- "Master advanced concepts in..."
- "Achieve fluency with..."
- "Become proficient in..."
- "Polish and perfect..."
- "Demonstrate expertise in..."

#### Flow (Orange) - Natural Rhythm
- "Work effortlessly with..."
- "Create projects using..."
- "Apply naturally in..."
- "Integrate into workflow..."
- "Build real applications with..."

#### Renewal (Red) - Integration
- "Review and consolidate..."
- "Reflect on journey with..."
- "Document learnings about..."
- "Prepare for next phase of..."
- "Integrate and synthesize..."

---

## 🎨 User Interface

### Goal Card Display

The goal appears in a beautiful card at the top of the Parva view:

```
┌─────────────────────────────────────┐
│ My Goal for this Parva          ✏️  │
│                                     │
│ Learn React Hooks and useState      │
│                                     │
└─────────────────────────────────────┘
```

- **Colored background** matching the theme (e.g., Violet for Beginning)
- **Pencil icon** on the right (only for current/future Parvas)
- **No goal set** → Shows prompt: "No goal set yet. Tap pencil to add one!"

### Edit Dialog

Tap the pencil icon to open:

```
┌─────────────────────────────────────┐
│ Set Your Goal                       │
│ Beginning Parva                     │
├─────────────────────────────────────┤
│                                     │
│ The foundation is laid. New habits  │
│ and intentions take root.           │
│                                     │
│ [Your Goal________________]         │
│ [_________________________]         │
│ [_________________________]         │
│                                     │
│ 💡 Suggestions based on "Beginning" │
│                                     │
│ ┌─ Learn the fundamentals of... ─┐ │
│ ┌─ Establish a foundation in...─┐ │
│ ┌─ Start exploring... ──────────┐ │
│ ┌─ Build basic knowledge of... ─┐ │
│ ┌─ Get introduced to... ────────┐ │
│                                     │
│ Tap any suggestion to use it        │
│                                     │
│        [Cancel]  [Save Goal]        │
└─────────────────────────────────────┘
```

**Features:**
- Multi-line text field for your goal
- Theme description for context
- 5 template suggestions
- Tap a suggestion to insert it as a starting point
- Edit further to make it your own

---

## 🚀 How to Use

### Setting a Goal

1. **Navigate to a Parva**
   - Home → Tap Maha-Parva → Tap any Parva petal

2. **Find the Goal Card**
   - At the top of the screen (both list and mandala views)

3. **Tap the Pencil Icon** ✏️
   - Only visible for current and future Parvas

4. **Choose Your Approach:**
   
   **Option A: Write from scratch**
   - Type your goal in the text field
   - Be specific and actionable
   
   **Option B: Use a template**
   - Tap any suggestion
   - It gets inserted into your text field
   - Edit to customize
   
   **Option C: Combine templates**
   - Tap multiple suggestions
   - They stack in your text field
   - Edit to create your perfect goal

5. **Save**
   - Tap "Save Goal" button
   - Goal is stored and displayed on the card

### Example Workflow

**Scenario:** Learning React

**Maha-Parva:** "Be awesome in React"

**Parva 1 (Beginning):**
1. Tap pencil icon
2. See prompt: "Learn the fundamentals of..."
3. Complete it: "Learn the fundamentals of React Hooks - useState, useEffect, and custom hooks"
4. Save
5. Result: Goal shows on card throughout this 49-day period

**Parva 2 (Practice):**
1. Tap pencil icon
2. See prompts about practice
3. Choose: "Build 3 small projects using hooks"
4. Save

**And so on...**

---

## 🔒 Editing Restrictions

### Can Edit:
- ✅ **Current Parva** - The 49-day period you're in right now
- ✅ **Future Parvas** - Any Parva that hasn't started yet
- ✅ **Unlimited edits** - Change your mind anytime!

### Cannot Edit:
- ❌ **Past Parvas** - Once a Parva ends, its goal is locked
- ❌ **Why?** To preserve your journey and learnings

**Visual Indicator:**
- **Editable Parvas** → Pencil icon visible ✏️
- **Past Parvas** → No pencil icon (view-only)

---

## 💡 Best Practices

### 1. **Be Specific**

❌ Bad: "Learn React"
✅ Good: "Learn React Hooks - useState, useEffect, useContext with 3 practice projects"

### 2. **Align with Theme**

Each theme has a natural flow:
- Beginning → Focus on fundamentals
- Practice → Focus on repetition and exercises
- Discernment → Focus on understanding patterns
- Ascent → Focus on advanced concepts
- Mastery → Focus on polish and expertise
- Flow → Focus on real applications
- Renewal → Focus on review and integration

### 3. **Plan Ahead (But Stay Flexible)**

- Set goals for next 2-3 Parvas ahead
- Adjust as you learn and grow
- Don't plan all 7 at once - let the journey unfold

### 4. **Use Templates as Starting Points**

Templates are prompts, not prescriptions:
- Start with a template
- Customize to your situation
- Add specifics
- Make it yours

### 5. **Review and Adjust**

At the start of each Parva:
- Review the goal you set
- Adjust if needed (before it starts)
- Make it realistic based on current knowledge

---

## 🎯 Example Journey: Learning React

### Maha-Parva
**Title:** "Become a React Expert"
**Description:** "Master React, hooks, state management, and build real-world applications"

### Parva Goals

1. **Beginning (Days 1-49)**
   - Goal: "Learn React fundamentals - JSX, components, props, and basic hooks (useState, useEffect)"

2. **Practice (Days 50-98)**
   - Goal: "Build 5 small projects: Todo app, Weather app, Notes app, Quiz app, Calculator"

3. **Discernment (Days 99-147)**
   - Goal: "Understand component patterns, when to use hooks vs classes, and performance optimization"

4. **Ascent (Days 148-196)**
   - Goal: "Learn advanced hooks (useReducer, useContext, useMemo, useCallback) and Context API"

5. **Mastery (Days 197-245)**
   - Goal: "Master React Router, form handling, API integration, and error boundaries"

6. **Flow (Days 246-294)**
   - Goal: "Build 2 real-world applications: E-commerce site and Social media dashboard"

7. **Renewal (Days 295-343)**
   - Goal: "Review all concepts, create a portfolio, document learnings, and plan next steps (Next.js/TypeScript)"

---

## 🛠️ Technical Details

### Data Model

```kotlin
data class Parva(
    val number: Int,           // 1-7
    val theme: CycleTheme,     // Auto: Beginning/Practice/etc.
    val startDate: LocalDate,
    val saptahas: List<Saptaha>,
    val customGoal: String = "" // YOUR GOAL! 🎯
)
```

### Properties

```kotlin
parva.isEditable  // true if current or future
parva.isPast      // true if ended
parva.isActive    // true if currently in this period
```

### Goal Templates

Stored in `CycleTheme.goalPrompts: List<String>`

---

## 📱 Screenshots (Conceptual)

### 1. Parva View with Goal
```
┌─────────────────────────────────────┐
│ ← Beginning Parva              ☰ ⚙  │
├─────────────────────────────────────┤
│                                     │
│ ┌─── My Goal ─────────────────✏️ ─┐│
│ │ Learn React Hooks and state    ││
│ │ management fundamentals        ││
│ └───────────────────────────────── ┘│
│                                     │
│     [Mandala visualization]         │
│                                     │
└─────────────────────────────────────┘
```

### 2. Edit Dialog
```
┌─────────────────────────────────────┐
│ Set Your Goal                       │
│ Beginning Parva (Violet)            │
├─────────────────────────────────────┤
│ [Text field with current goal]      │
│                                     │
│ 💡 Suggestions:                     │
│ • Learn the fundamentals of...      │
│ • Establish a foundation in...      │
│ • Start exploring...                │
│                                     │
│        [Cancel]  [Save Goal]        │
└─────────────────────────────────────┘
```

---

## 🎊 Summary

You can now:
- ✅ Set custom goals for each 49-day Parva
- ✅ Get smart suggestions based on the theme
- ✅ Plan flexibly ahead
- ✅ Lock past goals to preserve history
- ✅ See goals beautifully displayed in context

**Your journey, your goals, enhanced by the Parva structure!** 🪷✨

---

**Version:** 1.2.0  
**Feature:** Custom Goal System  
**Status:** ✅ Complete & Ready to Use

