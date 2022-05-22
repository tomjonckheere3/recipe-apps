<?php

namespace Database\Factories;

use Carbon\Carbon;
use App\Models\Notification;
use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Str;

class NotificationFactory extends Factory
{
    /**
     * The name of the factory's corresponding model.
     *
     * @var string
     */
    protected $model = Notification::class;

    /**
     * Define the model's default state.
     *
     * @return array
     */
    public function definition()
    {
        return [
            "userId" => rand(1, 20),
            "notificationContent" => "notification content",
            "date" => Carbon::now()->toDateTimeString()
        ];
    }
}
