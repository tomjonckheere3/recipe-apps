<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Notification extends Model
{
    use HasFactory;
    public $timestamps = false;

    protected $fillable = [
        "userId",
        "notificationContent",
        "date"
    ];

    protected $primaryKey = "notificationId";
}
